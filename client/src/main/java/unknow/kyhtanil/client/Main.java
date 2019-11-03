package unknow.kyhtanil.client;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;

import unknow.common.Cfg;
import unknow.kyhtanil.client.component.Archetypes;
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
import unknow.kyhtanil.common.component.ErrorComp.ErrorCode;
import unknow.kyhtanil.common.util.BaseUUIDManager;
import unknow.scene.builder.DynLayout;

public class Main implements ApplicationListener
	{
	private static final Logger log=LoggerFactory.getLogger(Main.class);
	public static Main self;

	private World world;

	public static enum Screen
		{
	LOGIN("login.xml"), CHARSELECT("charselect.xml"), CREATE("charcreate.xml"), GAME("layout.xml");

		private FileHandle file;

		private Screen(String file)
			{
			this.file=Gdx.files.internal(file);
			}

		public InputStream file()
			{
			return file.read();
			}
		}

	private Stage stage;
	private Viewport gameVp=new ExtendViewport(70, 46);
	private DynLayout dynLayout=new DynLayout();

	@Override
	public void create()
		{
		try
			{
			self=this;
			VisUI.load();

			stage=new Stage(new ScreenViewport());

			BaseUUIDManager manager=new BaseUUIDManager();

			Connection co=new Connection(Cfg.getSystemString("game.host"), Cfg.getSystemInt("game.port"));
			dynLayout.put("main", this);
			dynLayout.put("co", co);
			dynLayout.put("State", dynLayout.js.eval("Java.type('"+State.class.getName()+"')"));
			dynLayout.put("Screen", dynLayout.js.eval("Java.type('"+Screen.class.getName()+"')"));

			InputSystem inputSystem=new InputSystem(stage.getViewport());
			WorldConfiguration cfg=new WorldConfiguration();
			cfg.setSystem(new Archetypes());
			cfg.setSystem(manager);
			cfg.setSystem(inputSystem);
			cfg.setSystem(new TexManager());

			cfg.setSystem(new MovementSystem());
			cfg.setSystem(new RenderingSystem(gameVp));
			cfg.setSystem(co);

			cfg.setSystem(new ErrorSystem(this));
			cfg.setSystem(new LogResultSystem(this));
			cfg.setSystem(new PjInfoSystem(this));
			cfg.setSystem(new SpawnSystem());
			cfg.setSystem(new UpdateInfoSystem());
			cfg.setSystem(new DamageReportSystem());
			cfg.setSystem(new DespawnSystem());

			world=new World(cfg);

			Gdx.input.setInputProcessor(new InputMultiplexer(inputSystem, stage));
			stage.addActor(dynLayout);
			show(Screen.LOGIN);
			}
		catch (Exception e)
			{
			throw new RuntimeException(e);
			}
		}

	public void show(Screen screen)
		{
		try
			{
			dynLayout.load(new InputSource(screen.file()));
			}
		catch (Exception e)
			{
			e.printStackTrace();
			}
		}

	public void error(ErrorCode code)
		{
		VisLabel info=(VisLabel)dynLayout.get("info");
		if(info==null)
			return;
		switch (code)
			{
			case INVALID_LOGIN:
				info.setText("Login/pass error");
				break;
			case ALREADY_LOGGED:
				info.setText("Account already logged");
				break;
			case UNKNOWN_ERROR:
			default:
				info.setText("Unknown error occured");
			}
		}

	@Override
	public void render()
		{
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		world.delta=Gdx.graphics.getDeltaTime();
		world.process();
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		}

	public static float pixelToUnit(int px)
		{
		return px/8f;
		}

	@Override
	public void resize(int width, int height)
		{
		gameVp.update(width, height);
		stage.getViewport().update(width, height, true);
		}

	@Override
	public void pause()
		{
		}

	@Override
	public void resume()
		{
		}

	@Override
	public void dispose()
		{
		}
	}
