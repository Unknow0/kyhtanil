package unknow.kyhtanil.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.kotcrab.vis.ui.VisUI;

import unknow.common.Cfg;
import unknow.kyhtanil.client.component.Archetypes;
import unknow.kyhtanil.client.screen.CharSelectScreen;
import unknow.kyhtanil.client.screen.GameScreen;
import unknow.kyhtanil.client.screen.LoginScreen;
import unknow.kyhtanil.client.screen.WorldScreen;
import unknow.kyhtanil.client.system.InputSystem;
import unknow.kyhtanil.client.system.MovementSystem;
import unknow.kyhtanil.client.system.RenderingSystem;
import unknow.kyhtanil.client.system.TexManager;
import unknow.kyhtanil.client.system.net.Connection;
import unknow.kyhtanil.client.system.net.DamageReportSystem;
import unknow.kyhtanil.client.system.net.DespawnSystem;
import unknow.kyhtanil.client.system.net.ErrorSystem;
import unknow.kyhtanil.client.system.net.LogResultSystem;
import unknow.kyhtanil.client.system.net.PjInfoSystem;
import unknow.kyhtanil.client.system.net.SpawnSystem;
import unknow.kyhtanil.client.system.net.UpdateInfoSystem;
import unknow.kyhtanil.common.util.BaseUUIDManager;

public class Main extends Game
	{
	private static final Logger log=LoggerFactory.getLogger(Main.class);
	public static Main self;

	public LoginScreen login;
	private CharSelectScreen charSelect;
	private CharSelectScreen charCreate;
	private WorldScreen worldScreen;

	private World world;

	public void create()
		{
		try
			{
			self=this;
			VisUI.load();

			BaseUUIDManager manager=new BaseUUIDManager();

			Connection co=new Connection(Cfg.getSystemString("game.host"), Cfg.getSystemInt("game.port"));

			worldScreen=new WorldScreen();
			charSelect=new CharSelectScreen(co);
			login=new LoginScreen(co);

			InputSystem inputSystem=new InputSystem(worldScreen.gameViewpoint(), worldScreen, manager);
			worldScreen.set(inputSystem);
			WorldConfiguration cfg=new WorldConfiguration();
			cfg.setSystem(new Archetypes());
			cfg.setSystem(manager);
			cfg.setSystem(inputSystem);
			cfg.setSystem(new TexManager());

			cfg.setSystem(new MovementSystem());
			cfg.setSystem(new RenderingSystem(worldScreen.gameViewpoint()));
			cfg.setSystem(co);

			cfg.setSystem(new ErrorSystem(this));
			cfg.setSystem(new LogResultSystem(this, charSelect));
			cfg.setSystem(new PjInfoSystem(this, worldScreen));
			cfg.setSystem(new SpawnSystem());
			cfg.setSystem(new UpdateInfoSystem());
			cfg.setSystem(new DamageReportSystem());
			cfg.setSystem(new DespawnSystem());

			world=new World(cfg);

			setScreen(login);
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
		world.process();
		screen.render(world.delta);
		}

	public static GameScreen screen()
		{
		return (GameScreen)self.getScreen();
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
