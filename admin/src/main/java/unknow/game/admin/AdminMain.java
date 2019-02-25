package unknow.game.admin;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.MenuItem;

import unknow.game.admin.screen.AdminContent;
import unknow.game.admin.screen.LayoutEditorScreen;
import unknow.game.admin.screen.TilesetEditor;
import unknow.kyhtanil.common.maps.MapLayout;
import unknow.scene.builder.SceneBuilder;

public class AdminMain extends Game
	{
	public static AdminMain self;
	private static Lwjgl3Application application;

	public static Skin skin;

	private Stage stage;

	public MapLayout layout;

//	private Loader loader=new Loader("Update");

	private MenuItem loadMap;

	private TilesetEditor tilesetEditor;

	private SceneBuilder sceneBuilder=new SceneBuilder();
	private Cell<AdminContent> content;

	private LayoutEditorScreen layoutEditor;

	public void create()
		{
		self=this;
		VisUI.load();

		try
			{
			File f=new File("data/maps.layout");
			if(f.exists())
				{
				DataInputStream in=new DataInputStream(new FileInputStream(f));
				layout=new MapLayout(in);
				in.close();
				}
			else
				layout=new MapLayout();
			layoutEditor=new LayoutEditorScreen(layout);

			stage=new Stage(new ScreenViewport());
//			stage.setDebugAll(true);
//			tilesetEditor=new TilesetEditor(layout, sceneBuilder);
			sceneBuilder.addActor("tilesetedit", tilesetEditor);
			sceneBuilder.addActor("update", new ChangeListener()
				{
				public void changed(ChangeEvent event, Actor actor)
					{
//		XXX			loader.update();
					}
				});
			sceneBuilder.addActor("commit", new ChangeListener()
				{
				public void changed(ChangeEvent event, Actor actor)
					{
//		XXX			loader.commit();
					}
				});
			sceneBuilder.addActor("quit", new ChangeListener()
				{
				public void changed(ChangeEvent event, Actor actor)
					{
					System.exit(0);
					}
				});
			sceneBuilder.addActor("map.new", new ChangeListener()
				{
				public void changed(ChangeEvent event, Actor actor)
					{
					// TODO
					}
				});
			sceneBuilder.addActor("tileset.new", new ChangeListener()
				{
				public void changed(ChangeEvent event, Actor actor)
					{
//		XXX			tilesetEditor.newTileset();
					}
				});

			sceneBuilder.build("main.xml", stage.getRoot());
			content=sceneBuilder.getActor("content");
			}
		catch (Exception e)
			{
			throw new RuntimeException(e);
			}
		Gdx.input.setInputProcessor(stage);
		content.setActor(layoutEditor);
		}

	@Override
	public void render()
		{
		Gdx.gl.glClearColor(.2f, .2f, .2f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.getViewport().apply();
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		}

	@Override
	public void resize(int width, int height)
		{
		stage.getViewport().update(width, height, true);
		}

	public static float pixelToUnit(int px)
		{
		return px;
		}

	public static void main(String arg[]) throws Exception
		{
		Lwjgl3ApplicationConfiguration conf=new Lwjgl3ApplicationConfiguration();
		conf.setTitle("Game");
		conf.setWindowedMode(560, 368);
		conf.setResizable(true);
		application=new Lwjgl3Application(new AdminMain(), conf);
		}
	}
