package unknow.kyhtanil.client.screen;

import java.io.*;

import unknow.kyhtanil.client.*;

import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.viewport.*;
import com.kotcrab.vis.ui.widget.*;

public class LoginScreen extends GameScreen
	{
	private Stage stage;

	private VisLabel txt;
	private VisTextField login;
	private VisTextField pass;

	public LoginScreen()
		{
		login=new VisTextField("");
		pass=new VisTextField("");
		pass.setPasswordMode(true);
		pass.setPasswordCharacter('*');

		stage=new Stage(new ScreenViewport());
		VisTable table=new VisTable();
		table.setFillParent(true);
		stage.addActor(table);

		stage.addListener(new EventListener()
			{
				public boolean handle(Event event)
					{
					if(event instanceof InputEvent)
						{
						InputEvent e=(InputEvent)event;
						if(e.getType()==InputEvent.Type.keyDown&&e.getKeyCode()==Keys.ENTER)
							{
							login();
							}
						}
					return false;
					}
			});

		txt=new VisLabel("");
		table.add(txt).colspan(2);
		table.row();

		table.add(new VisLabel("Login"));
		table.add(login);
		table.row();
		table.add(new VisLabel("Pass"));
		table.add(pass);
		table.row();
		VisTextButton button=new VisTextButton("login");
		button.addListener(new ChangeListener()
			{
				@Override
				public void changed(ChangeEvent event, Actor actor)
					{
					login();
					}
			});

		table.add(button).colspan(2);
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

	private void login()
		{
		try
			{
			Main.co().login(login.getText(), pass.getText());
			}
		catch (IOException e)
			{ // TODO manage error
			e.printStackTrace();
			}
		}

	public void setError(String string)
		{
		txt.setText(string);
		}
	}
