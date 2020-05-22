package unknow.kyhtanil.client.graphics;

import java.io.InputStream;

import org.xml.sax.InputSource;

import com.badlogic.gdx.Gdx;
import com.kotcrab.vis.ui.widget.VisWindow;

import unknow.kyhtanil.client.Main;
import unknow.scene.builder.DynLayout;

/**
 * List of ingame window
 * 
 * @author unknow
 */
public enum GameWindow {
	/** the stats window */
	STATS("Stats", "ui/ingame/stats.xml"),
	/** the inventory window */
	INVENTORY("Inventory", "ui/ingame/inventory.xml");

	private static DynLayout ROOT;

	/**
	 * load all required dependencies
	 * 
	 * @param root root dynLayout
	 */
	public static void init(DynLayout root) {
		ROOT = root;
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

	private GameWindow(String title, String layout) {
		this.layout = layout;
		internal = new VisWindow(title);
	}

//	private static class Internal extends DynLayout {
//		public Internal() {
//			super(Main.dynContext);
//		}
//
//		@Override
//		public void draw(Batch batch, float parentAlpha) {
//			layout();
//			super.draw(batch, parentAlpha);
//		}
//	}

	/**
	 * close this window
	 */
	public void close() {
		internal.remove();
	}

	/**
	 * open the window
	 */
	public void show() {
		ROOT.addActor(internal);
		internal.pack();
		internal.centerWindow();
	}

	/**
	 * show if the windows is closed else close
	 */
	public void toggle() {
		if (internal.hasParent())
			close();
		else
			show();
	}
}
