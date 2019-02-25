package unknow.game.admin.actor;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.MenuBar;

import unknow.scene.builder.SceneBuilder;

public class Menu extends Group
	{
	private Actor menuBar;

	public Menu()
		{
		SceneBuilder sceneBuilder=new SceneBuilder();

		sceneBuilder.addActor("update", new ChangeListener()
			{
			public void changed(ChangeEvent event, Actor actor)
				{
//	XXX			loader.update();
				}
			});
		sceneBuilder.addActor("commit", new ChangeListener()
			{
			public void changed(ChangeEvent event, Actor actor)
				{
//	XXX			loader.commit();
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
//	XXX			tilesetEditor.newTileset();
				}
			});
		try
			{
			sceneBuilder.build("menu.xml", this);
			menuBar=this.getChildren().first();
			}
		catch (Exception e)
			{
			throw new RuntimeException(e);
			}
		}

	public Actor getRoot()
		{
		return menuBar;
		}
	}
