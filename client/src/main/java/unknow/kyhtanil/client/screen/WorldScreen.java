package unknow.kyhtanil.client.screen;

import java.io.*;
import java.sql.*;
import java.util.*;

import unknow.kyhtanil.client.*;
import unknow.kyhtanil.client.artemis.*;
import unknow.kyhtanil.client.component.*;
import unknow.kyhtanil.client.dao.*;
import unknow.kyhtanil.client.graphics.*;
import unknow.kyhtanil.client.system.*;
import unknow.kyhtanil.common.*;
import unknow.kyhtanil.common.UUID;

import com.artemis.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.utils.viewport.*;

public class WorldScreen extends GameScreen
	{
	private Stage stage;
	protected TileMap map;

	protected Viewport vp;
	protected TextureRegion mobTex; // XXX

	protected World world;

	protected SpriteBatch batch;

	protected UUIDManager manager;

	protected InputSystem inputSystem;

	private boolean loadMap=false;

	private Stats stat=new Stats();

	public WorldScreen(TileMap map)
		{
		this.vp=new ExtendViewport(70, 46);
		this.map=map;

		mobTex=new TextureRegion(new Texture(Gdx.files.internal("mob.png")));

		batch=new SpriteBatch();

		manager=new UUIDManager();
		inputSystem=new InputSystem(vp, this, manager);
		WorldConfiguration cfg=new WorldConfiguration();
		cfg.setSystem(manager);
		cfg.setSystem(inputSystem);
		cfg.setSystem(new MovementSystem());
		cfg.setSystem(new RenderingSystem(vp.getCamera()));

		world=new World(cfg);
		Builder.init(world);

		TextureRegion tex=new TextureRegion(new Texture(Gdx.files.internal("test.png")));
		State.entity=Builder.buildMob(0, 0, tex, Main.pixelToUnit(tex.getRegionWidth()), Main.pixelToUnit(tex.getRegionHeight()));
		VelocityComp v=Builder.getVelocity(State.entity);
		v.speed=1f;

		stage=new Stage();
		}

	public void show()
		{
		Gdx.input.setInputProcessor(new InputMultiplexer(inputSystem));

		manager.setUuid(State.entity, State.uuid);
		}

	public void center()
		{
		PositionComp p=Builder.getPosition(State.entity);
		float x=p.x;
		float y=p.y;
		if(x<getWidth()/2)
			x=getWidth()/2;
		if(y<getHeight()/2)
			y=getHeight()/2;
		vp.getCamera().position.set(x, y, 0);
		vp.getCamera().update();
		}

	public void render(float delta)
		{
		if(loadMap)
			{
			try
				{
				MapDao m=Db.getMap(State.pj.map);
				DataInputStream dis=new DataInputStream(new FileInputStream("data/map/"+m.name+".map"));
				map.getMap().load(dis);
				map.setTileset(new TileSet(Gdx.files.internal("data/tileset/"+m.tileset), 32, 32));
				loadMap=false;
				}
			catch (Exception e)
				{
				e.printStackTrace();
				}
			}
//		vp.apply();
		center();

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

		batch.setProjectionMatrix(vp.getCamera().combined);
		batch.begin();
		map.draw(batch, vp);
//		for(MobActor a:renderlist)
//			{
//			if(a==target)
//				batch.draw(targetTex, a.getX()-targetSize.x/2, a.getY()-targetSize.y/2, targetSize.x, targetSize.y);
//			a.draw(batch, vp);
//			}
		batch.end();

		world.setDelta(delta);
		world.process();

		stage.getViewport().apply();
		stage.act(delta);
		stage.draw();
		}

	public void resize(int width, int height)
		{
		vp.update(width, height);
		stage.getViewport().update(width, height, true);
		}

	public void manage(java.lang.Object data)
		{
		if(data instanceof Move)
			{
			Move move=(Move)data;

			int mob=manager.getEntity(move.uuid);
			Builder.update(mob, move);
			}
		else if(data instanceof PjInfo)
			{
			State.pj=(PjInfo)data;
			PositionComp p=Builder.getPosition(State.entity);
			p.x=State.pj.x;
			p.y=State.pj.y;

			loadMap=true;
			}
		else if(data instanceof Spawn)
			{
			final Spawn s=(Spawn)data;

			final String tex;
			String t=null;
			try
				{
				t=Db.getMobTex(s.type); // TODO cache
				}
			catch (SQLException e)
				{
				e.printStackTrace();
				}
			if(t!=null)
				tex=t;
			else
				tex="mob.png";

			Gdx.app.postRunnable(new Runnable()
				{
					public void run()
						{
						mobTex=new TextureRegion(new Texture(Gdx.files.internal(tex)));

						int mob=Builder.buildMob(s.x, s.y, mobTex, Main.pixelToUnit(mobTex.getRegionWidth()), Main.pixelToUnit(mobTex.getRegionHeight()));
						byte[] bytes=s.uuid.bytes;

						manager.setUuid(mob, new UUID(Arrays.copyOf(bytes, bytes.length))); // duplicate to avoid reuse
						}
				});
			}
		else if(data instanceof Despawn)
			{
			Despawn s=(Despawn)data;

			int mob=manager.getEntity(s.uuid);
			world.delete(mob);
			}
		}

	public float getWidth()
		{
		return vp.getWorldWidth();
		}

	public float getHeight()
		{
		return vp.getWorldHeight();
		}

	public void showStat()
		{
		stat.update();
		stage.addActor(stat);
		}
	}
