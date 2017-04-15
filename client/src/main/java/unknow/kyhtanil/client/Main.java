package unknow.kyhtanil.client;

import unknow.common.*;
import unknow.kyhtanil.client.artemis.*;
import unknow.kyhtanil.client.graphics.*;
import unknow.kyhtanil.client.screen.*;
import unknow.kyhtanil.client.system.*;
import unknow.kyhtanil.client.system.net.*;
import unknow.kyhtanil.common.component.*;
import unknow.kyhtanil.common.maps.*;

import com.artemis.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.kotcrab.vis.ui.*;

public class Main extends Game
	{
	private Connection client;
	public static Main self;

	public LoginScreen login;
	private CharSelectScreen charSelect;
	private CharSelectScreen charCreate;
	private WorldScreen worldScreen;

	private World world;
	private UUIDManager manager;
	private InputSystem inputSystem;

	public void create()
		{
		try
			{
			self=this;
			VisUI.load();
//			TileSet tileset=new TileSet(Gdx.files.internal("tileset.png"), 32, 32);
//			MapModel mapModel=new MapModel(100, 100);

			manager=new UUIDManager();

			worldScreen=new WorldScreen(manager);
			charSelect=new CharSelectScreen();
			login=new LoginScreen();

			inputSystem=new InputSystem(worldScreen.gameViewpoint(), worldScreen, manager);
			worldScreen.set(inputSystem);
			WorldConfiguration cfg=new WorldConfiguration();
			cfg.setSystem(manager);
			cfg.setSystem(inputSystem);
			cfg.setSystem(new MovementSystem());
			cfg.setSystem(new RenderingSystem(worldScreen.gameViewpoint().getCamera()));

			cfg.setSystem(new ErrorSystem(this));
			cfg.setSystem(new LogResultSystem(this, charSelect));
			cfg.setSystem(new PjInfoSystem(this, worldScreen));
			cfg.setSystem(new SpawnSystem());
			cfg.setSystem(new DespawnSystem());
			cfg.setSystem(new MoveSystem());
			cfg.setSystem(new DamageReportSystem());

			world=new World(cfg);
			Builder.init(world);

			TextureRegion tex=new TextureRegion(new Texture(Gdx.files.internal("test.png")));
			State.entity=Builder.buildMob(0, 0, tex, Main.pixelToUnit(tex.getRegionWidth()), Main.pixelToUnit(tex.getRegionHeight()), null);
			VelocityComp v=Builder.getVelocity(State.entity);
			v.speed=1f;

			setScreen(login);

			client=new Connection(Cfg.getSystemString("game.host"), Cfg.getSystemInt("game.port"), world);
			client.start();
			}
		catch (Exception e)
			{
			throw new RuntimeException(e);
			}
		}

	public void render()
		{
		world.delta=Gdx.graphics.getDeltaTime();
		if(screen==worldScreen)
			try
				{
				worldScreen.renderMap(world.delta);
				}
			catch (Exception e)
				{
				e.printStackTrace();
				}

		world.process();
		screen.render(world.delta);
		}

	public static GameScreen screen()
		{
		return (GameScreen)self.getScreen();
		}

	public static Connection co()
		{
		return self.client;
		}

	public void show(final GameScreen screen)
		{
		Gdx.app.postRunnable(new Runnable()
			{
				public void run()
					{
					self.setScreen(screen);
					}
			});
		}

	public static float pixelToUnit(int px)
		{
		return px/8f;
		}
	}
