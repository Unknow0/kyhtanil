package unknow.kyhtanil.client;

import java.io.InputStream;
import java.util.Arrays;

import org.xml.sax.InputSource;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;

import unknow.kyhtanil.client.component.Archetypes;
import unknow.kyhtanil.client.graphics.StatSelector;
import unknow.kyhtanil.client.system.InputSystem;
import unknow.kyhtanil.client.system.MovementSystem;
import unknow.kyhtanil.client.system.RenderingSystem;
import unknow.kyhtanil.client.system.State;
import unknow.kyhtanil.client.system.TexManager;
import unknow.kyhtanil.client.system.net.Connection;
import unknow.kyhtanil.client.system.net.DamageReportSystem;
import unknow.kyhtanil.client.system.net.DespawnSystem;
import unknow.kyhtanil.client.system.net.ErrorSystem;
import unknow.kyhtanil.client.system.net.LogResultSystem;
import unknow.kyhtanil.client.system.net.PjInfoSystem;
import unknow.kyhtanil.client.system.net.SpawnSystem;
import unknow.kyhtanil.client.system.net.UpdateInfoSystem;
import unknow.kyhtanil.common.Stats;
import unknow.kyhtanil.common.component.ErrorComp.ErrorCode;
import unknow.kyhtanil.common.component.StatBase;
import unknow.kyhtanil.common.component.StatPoint;
import unknow.kyhtanil.common.component.StatShared;
import unknow.kyhtanil.common.util.BaseUUIDManager;
import unknow.scene.builder.DynLayout;
import unknow.scene.builder.DynLayout.Attr;

public class Main implements ApplicationListener {
	public static Main self;

	private World world;

	public static enum Screen {
		LOGIN("ui/login.xml"), ACCOUNTCREATE("ui/accountcreate.xml"), CHARSELECT("ui/charselect.xml"), CHARCREATE("ui/charcreate.xml"), GAME("ui/layout.xml");

		private FileHandle file;

		private Screen(String file) {
			this.file = Gdx.files.internal(file);
		}

		public InputStream file() {
			return file.read();
		}
	}

	private Stage stage;
	private Viewport gameVp = new ExtendViewport(70, 46);
	private DynLayout dynLayout = new DynLayout();
	private I18NBundle i18n;

	@Override
	public void create() {
		try {
			self = this;
			VisUI.load();

			i18n = I18NBundle.createBundle(Gdx.files.internal("text"));
			stage = new Stage(new ScreenViewport());

			BaseUUIDManager manager = new BaseUUIDManager();

			Connection co = new Connection(Cfg.host, Cfg.port);
			dynLayout.put("main", this);
			dynLayout.put("co", co);
			dynLayout.put("state", State.state);
			dynLayout.put("i18n", i18n);
			for (Class<?> c : Arrays.asList(Stats.class, Align.class, Screen.class, StatBase.class, StatShared.class, StatPoint.class))
				dynLayout.put(c.getSimpleName(), dynLayout.js.eval("Java.type('" + c.getName() + "')"));
			dynLayout.addValueBuilder(StatSelector.class, new Attr[] { new Attr("value", "setValue"), new Attr("min", "setMin"), new Attr("max", "setMax") });

			InputSystem inputSystem = new InputSystem(gameVp);
			WorldConfiguration cfg = new WorldConfiguration();
			cfg.setSystem(new Archetypes());
			cfg.setSystem(manager);
			cfg.setSystem(inputSystem);
			cfg.setSystem(new TexManager());

			cfg.setSystem(new MovementSystem());
			cfg.setSystem(new RenderingSystem(gameVp));
			cfg.setSystem(co);
			cfg.setSystem(State.state);

			cfg.setSystem(new ErrorSystem(this));
			cfg.setSystem(new LogResultSystem(this));
			cfg.setSystem(new PjInfoSystem(this));
			cfg.setSystem(new SpawnSystem());
			cfg.setSystem(new UpdateInfoSystem());
			cfg.setSystem(new DamageReportSystem());
			cfg.setSystem(new DespawnSystem());

			world = new World(cfg);

			Gdx.input.setInputProcessor(new InputMultiplexer(inputSystem, stage));
			stage.addActor(dynLayout);
			show(Screen.LOGIN);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void show(Screen screen) {
		try (InputStream is = screen.file()) {
			dynLayout.load(new InputSource(is));
			System.out.println(dynLayout);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void error(ErrorCode code) {
		VisLabel info = (VisLabel) dynLayout.get("info");
		if (info == null)
			return;
		switch (code) {
		case INVALID_LOGIN:
			info.setText(i18n.get("error_invalid_login"));
			break;
		case ALREADY_LOGGED:
			info.setText(i18n.get("error_already_logged"));
			break;
		case NAME_ALREADY_USED:
			info.setText(i18n.get("error_name_already_used"));
			break;
		case UNKNOWN_ERROR:
		default:
			info.setText(i18n.get("error_unknown"));
		}
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		world.delta = Gdx.graphics.getDeltaTime();
		world.process();
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	public static float pixelToUnit(int px) {
		return px / 8f;
	}

	@Override
	public void resize(int width, int height) {
		gameVp.update(width, height);
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}
}
