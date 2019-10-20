package unknow.kyhtanil.client.screen;

import java.io.IOException;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;

import unknow.kyhtanil.client.State;
import unknow.kyhtanil.client.graphics.DrawableList;
import unknow.kyhtanil.client.system.net.Connection;
import unknow.kyhtanil.common.pojo.CharDesc;

public class CharSelectScreen extends GameScreen
	{
	private DrawableList<VisLabel> charList;
	private Connection connection;

	private final Listener listener=new Listener();

	public CharSelectScreen(Connection connection) throws NoSuchFieldException, SecurityException
		{
		this.connection=connection;
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
			connection.logChar(State.uuid, charDesc);
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
