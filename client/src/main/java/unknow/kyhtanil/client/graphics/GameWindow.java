package unknow.kyhtanil.client.graphics;

import java.io.InputStream;

import org.xml.sax.InputSource;

import com.badlogic.gdx.Gdx;
import com.kotcrab.vis.ui.widget.VisWindow;

import unknow.kyhtanil.client.Main;
import unknow.scene.builder.DynLayout;

public enum GameWindow {
	STATS("Stats", "ui/ingame/stats.xml");

	private static final DynLayout ROOT = Main.self.dynLayout();

	public static void init() {
		for (GameWindow w : values()) {
			DynLayout dynLayout = new DynLayout(Main.dynContext);
			try (InputStream is = Gdx.files.internal(w.layout).read()) {
				dynLayout.load(new InputSource(is));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			w.internal.add(dynLayout);
			w.internal.layout();
		}
	}

	private final VisWindow internal;
	private final String layout;

	GameWindow(String title, String layout) {
		this.layout = layout;
		internal = new VisWindow(title);
	}

	public void close() {
		internal.remove();
	}

	public void show() {
		ROOT.addActor(internal);
		internal.pack();
		internal.centerWindow();
	}

	public void toggle() {
		if (internal.hasParent())
			close();
		else
			show();
	}
}
