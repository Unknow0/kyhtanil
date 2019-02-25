package unknow.kyhtanil.client.screen;

import java.io.*;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.*;
import org.xml.sax.SAXException;

import unknow.kyhtanil.client.*;
import unknow.kyhtanil.client.artemis.*;
import unknow.kyhtanil.client.graphics.*;
import unknow.kyhtanil.client.system.*;
import unknow.kyhtanil.common.component.*;
import unknow.kyhtanil.common.maps.*;
import unknow.scene.builder.SceneBuilder;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.utils.viewport.*;
import com.kotcrab.vis.ui.widget.*;

public class WorldScreen extends GameScreen
	{
	private static final Logger log=LoggerFactory.getLogger(WorldScreen.class);
	protected TileSet tileset;
	protected MapLayout layout;

	protected Viewport vp;

	protected UUIDManager manager;
	protected InputSystem inputSystem;

	protected SpriteBatch batch;

//	private boolean loadMap=false;

	private Stats stat=new Stats();

	/** list of opened windows */
	private LinkedList<VisWindow> windows=new LinkedList<VisWindow>();

	public WorldScreen(UUIDManager manager) throws FileNotFoundException, IOException, SAXException, ParserConfigurationException
		{
		this.vp=new ExtendViewport(70, 46);

		batch=new SpriteBatch();

		this.manager=manager;
		this.layout=new MapLayout(new DataInputStream(new FileInputStream("data/maps.layout")));

		stat.setVisible(false);
		stage.addActor(stat);

		SceneBuilder sceneBuilder=new SceneBuilder();
		sceneBuilder.addActor("hpbar", new HpBarActor());
		sceneBuilder.build("layout.xml", stage.getRoot());
		}

	public void set(InputSystem inputSystem)
		{
		this.inputSystem=inputSystem;
		}

	public void show()
		{
		Gdx.input.setInputProcessor(new InputMultiplexer(stage, inputSystem));
		}

	public void renderMap(float delta) throws IOException
		{
//		if(loadMap)
//			{
//			try
//				{
//				MapDao m=Db.getMap(State.pj.map);
//				DataInputStream dis=new DataInputStream(new FileInputStream("data/map/"+m.name+".map"));
//				map.getMap().load(dis);
//				map.setTileset(new TileSet(Gdx.files.internal("data/tileset/"+m.tileset), 32, 32));
//				loadMap=false;
//				}
//			catch (Exception e)
//				{
//				e.printStackTrace();
//				}
//			}
		PositionComp p=Builder.getPosition(State.entity);
		float x=p.x;
		float y=p.y;
		if(x<getWidth()/2)
			x=getWidth()/2;
		if(y<getHeight()/2)
			y=getHeight()/2;
		vp.getCamera().position.set(x, y, 0);
		vp.getCamera().update();

		batch.setProjectionMatrix(vp.getCamera().combined);
		batch.begin();
		layout.draw(batch, Main.pixelToUnit(1), vp);
		batch.end();
		}

	public void render(float delta)
		{
//		vp.apply();

//		renderlist.clear();
//		synchronized (objects)
//			{
//			renderlist.addAll(objects.values());
//			}
//		for(MobActor a:renderlist)
//			a.act(delta);
//		Vector2 d=vp.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
//		if(target!=null)
//			{
//			d.set(target.getX(), target.getY());
//			}
//		pj.setRotation((float)Math.atan2(d.y-pj.getY(), d.x-pj.getX()));

//		for(MobActor a:renderlist)
//			{
//			if(a==target)
//				batch.draw(targetTex, a.getX()-targetSize.x/2, a.getY()-targetSize.y/2, targetSize.x, targetSize.y);
//			a.draw(batch, vp);
//			}
//		batch.end();

		stage.getViewport().apply();
		stage.act(delta);
		stage.draw();
		}

	public void resize(int width, int height)
		{
		vp.update(width, height);
		stage.getViewport().update(width, height, true);
		}

	public Viewport gameViewpoint()
		{
		return vp;
		}

	public float getWidth()
		{
		return vp.getWorldWidth();
		}

	public float getHeight()
		{
		return vp.getWorldHeight();
		}

	public void toggleStat()
		{
		if(stat.isVisible())
			{
			stat.setVisible(false);
			windows.remove(stat);
			}
		else
			{
			stat.update();
			stat.centerWindow();
			stat.setVisible(true);
			windows.push(stat);
			}
		}

	public void closeLast()
		{
		VisWindow w=windows.poll();
		if(w!=null)
			w.setVisible(false);
		}
	}