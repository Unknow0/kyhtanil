package unknow.kyhtanil.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.kotcrab.vis.ui.VisUI;

import unknow.common.Cfg;
import unknow.kyhtanil.client.artemis.Builder;
import unknow.kyhtanil.client.artemis.UUIDManager;
import unknow.kyhtanil.client.screen.CharSelectScreen;
import unknow.kyhtanil.client.screen.GameScreen;
import unknow.kyhtanil.client.screen.LoginScreen;
import unknow.kyhtanil.client.screen.WorldScreen;
import unknow.kyhtanil.client.system.InputSystem;
import unknow.kyhtanil.client.system.MovementSystem;
import unknow.kyhtanil.client.system.RenderingSystem;
import unknow.kyhtanil.client.system.TexManager;
import unknow.kyhtanil.client.system.net.DamageReportSystem;
import unknow.kyhtanil.client.system.net.DespawnSystem;
import unknow.kyhtanil.client.system.net.ErrorSystem;
import unknow.kyhtanil.client.system.net.LogResultSystem;
import unknow.kyhtanil.client.system.net.MoveSystem;
import unknow.kyhtanil.client.system.net.PjInfoSystem;
import unknow.kyhtanil.client.system.net.SpawnSystem;
import unknow.kyhtanil.common.component.SpriteComp;
import unknow.kyhtanil.common.component.VelocityComp;

public class Main extends Game
	{
	private static final Logger log=LoggerFactory.getLogger(Main.class);
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

			manager=new UUIDManager();

			worldScreen=new WorldScreen(manager);
			charSelect=new CharSelectScreen();
			login=new LoginScreen();

			inputSystem=new InputSystem(worldScreen.gameViewpoint(), worldScreen, manager);
			worldScreen.set(inputSystem);
			WorldConfiguration cfg=new WorldConfiguration();
			cfg.setSystem(manager);
			cfg.setSystem(inputSystem);
			cfg.setSystem(new TexManager());
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

			setScreen(login);

			client=new Connection(Cfg.getSystemString("game.host"), Cfg.getSystemInt("game.port"), world);
			client.start();
			}
		catch (Exception e)
			{
			throw new RuntimeException(e);
			}
		}

	@Override
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
