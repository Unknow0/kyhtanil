package unknow.kyhtanil.client.screen;

import unknow.kyhtanil.client.*;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.kotcrab.vis.ui.widget.*;

public class LoginScreen extends GameScreen
	{
	private VisLabel txt;
	private VisTextField login;
	private VisTextField pass;

	public LoginScreen()
		{
		login=new VisTextField("");
		pass=new VisTextField("");
		pass.setPasswordMode(true);
		pass.setPasswordCharacter('*');

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
		VisTable t2=new VisTable();
		table.add(t2).colspan(2).center();
		VisTextButton button=new VisTextButton("create");
		button.addListener(new ChangeListener()
			{
				@Override
				public void changed(ChangeEvent event, Actor actor)
					{
					create();
					}
			});

		t2.add(button);
		button=new VisTextButton("login");
		button.addListener(new ChangeListener()
			{
				@Override
				public void changed(ChangeEvent event, Actor actor)
					{
					login();
					}
			});

		t2.add(button);
		}

	private void login()
		{
		try
			{
			Main.co().login(login.getText(), pass.getText());
			}
		catch (Exception e)
			{ // TODO manage error
			e.printStackTrace();
			}
		}

	private void create()
		{
		try
			{
			Main.co().createAccount(login.getText(), pass.getText());
			}
		catch (Exception e)
			{ // TODO manage error
			e.printStackTrace();
			}
		}

	public void setError(String string)
		{
		txt.setText(string);
		}
	}
