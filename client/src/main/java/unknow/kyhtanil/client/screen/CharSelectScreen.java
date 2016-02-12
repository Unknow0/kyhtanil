package unknow.kyhtanil.client.screen;

import java.io.*;

import unknow.kyhtanil.client.*;
import unknow.kyhtanil.common.*;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.viewport.*;
import com.kotcrab.vis.ui.widget.*;

public class CharSelectScreen extends GameScreen
	{
	private Stage stage;

	private Table root;

	private final Listener listener=new Listener();

	public CharSelectScreen()
		{
		stage=new Stage(new ScreenViewport());
		root=new Table();
		root.setFillParent(true);
		stage.addActor(root);

		stage.addListener(listener);

//		table.row();
//
//		table.add(new Label("Login", skin));
//		table.add(login);
//		table.row();
//		table.add(new Label("Pass", skin));
//		table.add(pass);
//		table.row();
//		TextButton button=new TextButton("login", skin);
//		button.addListener(new ChangeListener()
//			{
//				@Override
//				public void changed(ChangeEvent event, Actor actor)
//					{
//					login();
//					}
//			});
//
//		table.add(button).colspan(2);
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

	public void setCharList(java.util.List<CharDesc> list)
		{
		root.clear();
		for(CharDesc c:list)
			{
			VisLabel label=new VisLabel(c.name+" ("+c.level+")");
			label.setUserObject(c);
			root.add(label);
			root.row();
			}
		}

	public void render(float delta)
		{
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(delta);
		stage.draw();
		}

	public void resize(int width, int height)
		{
		stage.getViewport().update(width, height, true);
		}

	public void show()
		{
		Gdx.input.setInputProcessor(stage);
		}

	public void dispose()
		{
		stage.dispose();
		}

	private class Listener extends ClickListener
		{
		public void clicked(InputEvent e, float x, float y)
			{
			if(getTapCount()==2)
				{
				Actor hit=stage.hit(e.getStageX(), e.getStageY(), false);
				if(hit!=null&&hit.getUserObject()!=null&&hit.getUserObject() instanceof CharDesc)
					{
					login((CharDesc)hit.getUserObject());
					}
				}
			}
		}
	}
