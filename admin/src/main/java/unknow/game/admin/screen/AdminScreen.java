package unknow.game.admin.screen;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.regex.*;

import javax.xml.parsers.*;

import org.xml.sax.*;

import unknow.common.*;
import unknow.kyhtanil.client.screen.*;
import unknow.kyhtanil.common.maps.*;
import unknow.scene.builder.*;
import unknow.sync.*;
import unknow.game.admin.actor.Menu;

import com.badlogic.gdx.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.utils.viewport.*;
import com.kotcrab.vis.ui.widget.*;

public abstract class AdminScreen extends GameScreen {
    protected static final Menu menu = new Menu();
    protected Loader loader = new Loader("Update");

    protected MenuItem loadMap;

    private static final List<AdminScreen> screens = new ArrayList<AdminScreen>();

    protected TilesetEditor tilesetEditor;
    protected MapLayout layout;

    protected SceneBuilder sceneBuilder = new SceneBuilder();

    public AdminScreen(MapLayout layout) throws SAXException, IOException, ParserConfigurationException {
	stage = new Stage(new ScreenViewport());
	screens.add(this);
	this.layout = layout;
	tilesetEditor = new TilesetEditor(layout, sceneBuilder);
	sceneBuilder.addActor("tilesetedit", tilesetEditor);
	sceneBuilder.addActor("menu", menu.getRoot());
    }

    private static final Pattern data = Pattern.compile("data/.*(?<!db\\.lck)$");

    protected class Loader extends VisWindow implements SyncListener, Runnable {
	private VisLabel taskLabel = new VisLabel("Updating...");
	private VisProgressBar task = new VisProgressBar(0, 100, 1, false);
	private Thread thread;
	private boolean commit;

	private Log listener;

	public Loader(String title) {
	    super(title);
	    add(taskLabel);
	    row();
	    add(task);
	    setVisible(true);
	    setModal(true);
	    listener = new Log();
	}

	public void show() {
	    task.setRange(0, 1);
	    task.setValue(0);
	    taskLabel.setText("");
	    stage.addActor(this);
	}

	public void update() {
	    if (thread != null)
		thread.interrupt();
	    commit = false;
	    thread = new Thread(this);
	    thread.start();
	}

	public void commit() {
	    if (thread != null)
		thread.interrupt();
	    commit = true;
	    thread = new Thread(this);
	    thread.start();
	}

	public void run() {
	    try {
		SyncClient sync = new SyncClient(Cfg.getSystemString("updater.host"), Cfg.getSystemInt("updater.port"), "./");
		sync.setListener(loader);

		loader.show();
		try {
		    DriverManager.getConnection("jdbc:derby:;shutdown=true");
		} catch (SQLException e) {
		    // ok
		}
		if (commit)
		    sync.commit(Cfg.getSystemString("updater.login"), Cfg.getSystemString("updater.pass"), Cfg.getSystemString("updater.project"), data);
		else
		    sync.update(Cfg.getSystemString("updater.login"), Cfg.getSystemString("updater.pass"), Cfg.getSystemString("updater.project"), false, data);
		sync.close();
	    } catch (Exception e) {
		showError("failed to update/commit", e);
	    }
	    try { // force reloading derby driver to reconnect db
		Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
	    } catch (Exception e) {
		showError("failed to restart db", e);
	    }
	    try {
		// if(!commit)
		// AdminScreen.loadTileMap();
	    } catch (Exception e) {
		showError("failed to reload data", e);
	    }
	    thread = null;
	}

	public void startUpdate(String project, int modified, int news, int delete) {
	    listener.startUpdate(project, modified, news, delete);
	    task.setRange(0, modified + news);
	}

	public void startFile(String name) {
	    listener.startFile(name);
	    Gdx.app.postRunnable(new UpdateLabel("Updating '" + name + "'"));
	}

	public void startCheckFile(String name) {
	    listener.startCheckFile(name);
	    Gdx.app.postRunnable(new UpdateLabel("Checking '" + name + "'"));
	}

	public void doneCheckFile(String name) {
	    listener.doneCheckFile(name);
	    task.setValue(task.getValue() + .5f);
	}

	public void startReconstruct(String name) {
	    listener.startReconstruct(name);
	    Gdx.app.postRunnable(new UpdateLabel("Reconstructing '" + name + "'"));
	}

	public void updateReconstruct(String name, float rate) {
	    listener.updateReconstruct(name, rate);
	    // XXX
	}

	public void doneReconstruct(String name, long fileSize, boolean ok) {
	    listener.doneReconstruct(name, fileSize, ok);
	    task.setValue(task.getValue() + (ok ? .5f : -.5f));
	}

	public void doneFile(String name, long fileSize) {
	    listener.doneFile(name, fileSize);
	}

	public void doneUpdate(String project) {
	    listener.doneUpdate(project);
	    stage.getActors().removeValue(this, true);
	}

	private class UpdateLabel implements Runnable {
	    private String label;

	    public UpdateLabel(String label) {
		this.label = label;
	    }

	    public void run() {
		taskLabel.setText(label);
	    }
	}
    }
}
