package unknow.kyhtanil.client;

import org.apache.logging.log4j.*;

import unknow.common.*;
import unknow.common.tools.*;
import unknow.json.*;
import unknow.kyhtanil.client.graphics.*;
import unknow.kyhtanil.client.screen.*;
import unknow.kyhtanil.common.*;

import com.badlogic.gdx.*;
import com.kotcrab.vis.ui.*;

public class Main extends Game
	{
	private static final Logger log=LogManager.getFormatterLogger(Main.class);
	private Connection client;
	public static Main self;

	private LoginScreen login;
	private CharSelectScreen charSelect;
	private WorldScreen world;

	public void create()
		{
		self=this;
		VisUI.load();
		TileSet tileset=new TileSet(Gdx.files.internal("tileset.png"), 32, 32);
		MapModel mapModel=new MapModel(100, 100);

		TileMap map=new TileMap(mapModel, tileset);
		world=new WorldScreen(map);

		charSelect=new CharSelectScreen();

		login=new LoginScreen();
		setScreen(login);

		try
			{
			client=new Connection(Cfg.getSystemString("game.host"), Cfg.getSystemInt("game.port"));
			client.start();
			}
		catch (JsonException e)
			{
			throw new RuntimeException(e);
			}
		}

	public static GameScreen screen()
		{
		return (GameScreen)self.getScreen();
		}

	public static Connection co()
		{
		return self.client;
		}

	public void show(final GameScreen screen)
		{
		Gdx.app.postRunnable(new Runnable()
			{
				public void run()
					{
					self.setScreen(screen);
					}
			});
		}

	public void manage(Object data)
		{
		log.debug("%s: %s", data==null?"":data.getClass().getSimpleName(), StringTools.toJson(data, true));
		if(data==null)
			{
			if(self.screen instanceof LoginScreen)
				((LoginScreen)screen).setError("Login/pass error");
			}
		else if(data instanceof LogResult)
			{
			LogResult r=(LogResult)data;
			State.uuid=r.uuid;
			charSelect.setCharList(r.characters);
			show(charSelect);
			}
		else if(data instanceof PjInfo)
			{
			world.manage(data);
			show(world);
			}
		else
			{
			world.manage(data);
			}
		}

	public static float pixelToUnit(int px)
		{
		return px/8f;
		}
	}
