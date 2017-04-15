package unknow.game.admin;

import java.io.*;

import unknow.game.admin.screen.*;
import unknow.kyhtanil.common.maps.*;
import unknow.kyhtanil.common.maps.MapLayout.MapEntry;

import com.badlogic.gdx.*;
import com.badlogic.gdx.backends.lwjgl3.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.kotcrab.vis.ui.*;

public class AdminMain extends Game
	{
	public static AdminMain self;

	public static Skin skin;

	public MapEditorScreen mapEditor;
	public LayoutEditorScreen layoutEditor;

	public MapLayout layout;

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
			mapEditor=new MapEditorScreen(layout);
			layoutEditor=new LayoutEditorScreen(layout);
			setScreen(layoutEditor);
//			AdminScreen.loadTileMap();
			}
		catch (Exception e)
			{
			throw new RuntimeException(e);
			}
		}

	public void showEditor(MapEntry selected)
		{
		try
			{
			mapEditor.setEntry(selected);
			setScreen(mapEditor);
			}
		catch (Exception e)
			{
			e.printStackTrace();
			}
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
		new Lwjgl3Application(new AdminMain(), conf);
		}
	}
