package unknow.kyhtanil.client.screen;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.widget.VisWindow;

import unknow.kyhtanil.client.graphics.Stats;
import unknow.kyhtanil.client.system.InputSystem;
import unknow.scene.builder.SceneBuilder;

public class WorldScreen extends GameScreen
	{
	protected Viewport vp;

	protected InputSystem inputSystem;

	protected SpriteBatch batch;

	private Stats stat=new Stats();

	/** list of opened windows */
	private LinkedList<VisWindow> windows=new LinkedList<VisWindow>();

	public WorldScreen() throws FileNotFoundException, IOException, SAXException, ParserConfigurationException
		{
		this.vp=new ExtendViewport(70, 46);

		batch=new SpriteBatch();

		stat.setVisible(false);
		stage.addActor(stat);

		SceneBuilder sceneBuilder=new SceneBuilder();
		sceneBuilder.addValue("width", stage.getWidth());
		sceneBuilder.addValue("height", stage.getHeight());
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

		}

	public void render(float delta)
		{
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