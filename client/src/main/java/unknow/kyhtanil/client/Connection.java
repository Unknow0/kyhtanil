package unknow.kyhtanil.client;

import java.io.*;

import org.apache.logging.log4j.*;

import unknow.kyhtanil.common.*;

import com.badlogic.gdx.*;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.*;
import com.esotericsoftware.kryo.*;
import com.esotericsoftware.kryo.io.*;
import com.esotericsoftware.kryo.io.Input;

public class Connection extends Thread
	{
	private static final Logger log=LogManager.getFormatterLogger(Connection.class);
	private final Kryo kryo;

	private Socket co;
	private Input in;
	private Output out;

	public Connection(String host, int port)
		{
		log.info("connecting to %s:%d", host, port);
		SocketHints hints=new SocketHints();
		hints.keepAlive=true;
		co=Gdx.net.newClientSocket(Protocol.TCP, host, port, hints);

		in=new Input(co.getInputStream());
		out=new Output(co.getOutputStream());

		kryo=new Kryo();
		kryo.setReferences(false);
		// TODO init
		}

	public boolean checkVersion() throws IOException
		{
		// TODO
		return true;
		}

	public void login(String login, String pass) throws IOException
		{
		kryo.writeClassAndObject(out, new Login(login, pass));
		out.flush();
		}

	public void update(UUID id, float x, float y, float dirX, float dirY) throws IOException
		{
		kryo.writeClassAndObject(out, new Move(id, x, y, dirX, dirY));
		out.flush();
		}

	public void logChar(UUID uuid, CharDesc charDesc) throws IOException
		{
		kryo.writeClassAndObject(out, new LogChar(uuid, charDesc.id));
		out.flush();
		}

	public void attack(UUID uuid, int attId, UUID target, float x, float y) throws IOException
		{
		kryo.writeClassAndObject(out, new Attack(uuid, attId, target==null?new Point(x, y):target));
		out.flush();
		}

	public void run()
		{
		try
			{
			Kryo kryo=new Kryo();
			kryo.setReferences(false);
			// init

			while (true)
				{
				Main.self.manage(kryo.readClassAndObject(in));
				}
			}
		catch (Exception e)
			{
			// TODOs
			e.printStackTrace();
			}
		}
	}
