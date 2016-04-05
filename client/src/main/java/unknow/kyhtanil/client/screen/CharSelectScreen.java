package unknow.kyhtanil.client.screen;

import java.io.*;

import unknow.kyhtanil.client.*;
import unknow.kyhtanil.client.graphics.*;
import unknow.kyhtanil.common.pojo.*;

import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.*;
import com.kotcrab.vis.ui.widget.*;

public class CharSelectScreen extends GameScreen
	{
	private DrawableList<VisLabel> charList;

	private final Listener listener=new Listener();

	public CharSelectScreen() throws NoSuchFieldException, SecurityException
		{
		charList=new DrawableList<>();

		stage.addListener(listener);

		VisTable table=new VisTable();
		stage.addActor(table);

		table.setFillParent(true);
		table.add(charList);
		table.row();
		VisTable t2=new VisTable();
		table.add(t2).center();

		}

	private void login(CharDesc charDesc)
		{
		try
			{
			Main.co().logChar(State.uuid, charDesc);
			}
		catch (IOException e)
			{ // TODO manage error
			e.printStackTrace();
			}
		}

	public void setCharList(CharDesc[] list)
		{
		Array<VisLabel> items=charList.getItems();
		items.clear();
		for(CharDesc c:list)
			{
			VisLabel label=new VisLabel(c.name+" ("+c.level+")");
			label.setUserObject(c);
			label.addListener(listener);
			items.add(label);
			}
		charList.invalidateHierarchy();
		charList.getSelection().validate();
		}

	private class Listener extends ClickListener
		{
		public void clicked(InputEvent e, float x, float y)
			{
			if(getTapCount()==2)
				{
				Actor hit=e.getListenerActor();//stage.hit(e.getStageX(), e.getStageY(), false);
				if(hit!=null&&hit.getUserObject()!=null&&hit.getUserObject() instanceof CharDesc)
					{
					login((CharDesc)hit.getUserObject());
					}
				}
			}
		}
	}
