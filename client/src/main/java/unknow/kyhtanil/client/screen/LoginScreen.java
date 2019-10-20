package unknow.kyhtanil.client.screen;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;

import unknow.kyhtanil.client.system.net.Connection;

public class LoginScreen extends GameScreen
	{
	private VisLabel txt;
	private VisTextField login;
	private VisTextField pass;

	private Connection connection;

	public LoginScreen(Connection connection)
		{
		this.connection=connection;
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
			connection.login(login.getText(), pass.getText());
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
			connection.createAccount(login.getText(), pass.getText());
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
