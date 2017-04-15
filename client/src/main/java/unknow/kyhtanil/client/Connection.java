package unknow.kyhtanil.client;

import java.io.*;
import java.security.*;
import java.util.*;

import org.slf4j.*;

import unknow.kyhtanil.common.*;
import unknow.kyhtanil.common.component.*;
import unknow.kyhtanil.common.pojo.*;
import unknow.kyhtanil.common.pojo.UUID;
import unknow.kyhtanil.common.util.*;

import com.artemis.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.*;
import com.esotericsoftware.kryo.io.*;
import com.esotericsoftware.kryo.io.Input;

public class Connection extends Thread
	{
	private static final Logger log=LoggerFactory.getLogger(Connection.class);
	private final Kryos kryo;

	private Socket co;
	private Input in;
	private Output out;

	private ComponentMapper<BooleanComp> done;

	public Connection(String host, int port, World world) throws Exception
		{
		setDaemon(true);

		log.info("connecting to {}:{}", host, port);
		SocketHints hints=new SocketHints();
		hints.keepAlive=true;
		co=Gdx.net.newClientSocket(Protocol.TCP, host, port, hints);

		in=new Input(co.getInputStream());
		out=new Output(co.getOutputStream());

		kryo=new Kryos(world, BooleanComp.class);
		kryo.write(out, kryo.hash());
		byte[] h=(byte[])kryo.read(in);
		if(!Arrays.equals(h, kryo.hash()))
			throw new Exception("Invalide version");

		done=ComponentMapper.getFor(BooleanComp.class, world);
		}

	public void createAccount(String login, String pass) throws IOException, NoSuchAlgorithmException
		{
		MessageDigest md=MessageDigest.getInstance("SHA-512");
		md.update(login.toLowerCase().getBytes("UTF8"));
		md.update((byte)':');
		md.update(pass.getBytes("UTF8"));
		kryo.write(out, new CreateAccount(login, md.digest()));
		}

	public void login(String login, String pass) throws IOException, NoSuchAlgorithmException
		{
		MessageDigest md=MessageDigest.getInstance("SHA-512");
		md.update(login.toLowerCase().getBytes("UTF8"));
		md.update((byte)':');
		md.update(pass.getBytes("UTF8"));
		kryo.write(out, new Login(login, md.digest()));
		}

	public void update(UUID id, float x, float y, float direction) throws IOException
		{
		kryo.write(out, new Move(id, x, y, direction));
		}

	public void logChar(UUID uuid, CharDesc charDesc) throws IOException
		{
		kryo.write(out, new LogChar(uuid, charDesc.id));
		}

	public void attack(UUID uuid, int attId, UUID target, float x, float y) throws IOException
		{
		kryo.write(out, new Attack(uuid, attId, target==null?new Point(x, y):target));
		}

	public void run()
		{
		try
			{
			while (true)
				{
				Object o=kryo.read(in);
				Integer e=ArtemisInstantiator.lastCreated();
				BooleanComp b=done.get(e);
				b.value=true;
				log.debug("{}", o);
				}
			}
		catch (Exception e)
			{
			// TODOs
			e.printStackTrace();
			}
		}
	}
