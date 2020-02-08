package unknow.kyhtanil.client.system.net;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artemis.BaseSystem;
import com.artemis.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import unknow.kyhtanil.common.component.account.CreateAccount;
import unknow.kyhtanil.common.component.account.LogChar;
import unknow.kyhtanil.common.component.account.Login;
import unknow.kyhtanil.common.component.net.Attack;
import unknow.kyhtanil.common.component.net.Move;
import unknow.kyhtanil.common.pojo.CharDesc;
import unknow.kyhtanil.common.pojo.Point;
import unknow.kyhtanil.common.pojo.UUID;
import unknow.kyhtanil.common.util.Kryos;

public class Connection extends BaseSystem implements Runnable {
	private static final Logger log = LoggerFactory.getLogger(Connection.class);
	private final Kryos kryo;

	private List<Component> list = new ArrayList<>();

	private Thread t;
	private Socket co;
	private Input in;
	private Output out;

	public Connection(String host, int port) throws Exception {
		log.info("connecting to {}:{}", host, port);
		SocketHints hints = new SocketHints();
		hints.keepAlive = true;
		co = Gdx.net.newClientSocket(Protocol.TCP, host, port, hints);

		in = new Input(co.getInputStream());
		out = new Output(co.getOutputStream());

		kryo = new Kryos();
		kryo.write(out, kryo.hash());
		byte[] h = (byte[]) kryo.read(in);
		if (!Arrays.equals(h, kryo.hash()))
			throw new Exception("Invalide version");

		t = new Thread(this);
		t.setDaemon(true);
		t.start();
	}

	public void createAccount(String login, String pass) throws IOException, NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-512");
		md.update(login.toLowerCase().getBytes("UTF8"));
		md.update((byte) ':');
		md.update(pass.getBytes("UTF8"));
		kryo.write(out, new CreateAccount(login, md.digest()));
	}

	public void login(String login, String pass) throws IOException, NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-512");
		md.update(login.toLowerCase().getBytes("UTF8"));
		md.update((byte) ':');
		md.update(pass.getBytes("UTF8"));
		kryo.write(out, new Login(login, md.digest()));
	}

	public void update(UUID id, float x, float y, float direction) throws IOException {
		kryo.write(out, new Move(id, x, y, direction));
	}

	public void logChar(UUID uuid, CharDesc charDesc) throws IOException {
		kryo.write(out, new LogChar(uuid, charDesc.id));
	}

	public void attack(UUID uuid, int attId, UUID target, float x, float y) throws IOException {
		kryo.write(out, new Attack(uuid, attId, target == null ? new Point(x, y) : target));
	}

	@Override
	public void run() {
		try {
			while (true) {
				Object o = kryo.read(in);

				log.info("{}", o);
				synchronized (list) {
					list.add((Component) o);
				}
			}
		} catch (Exception e) {
			// TODOs
			e.printStackTrace();
		}
	}

	@Override
	protected void processSystem() {
		synchronized (list) {
			for (Component c : list) {
				int create = world.create();
				world.edit(create).add(c);
			}
			list.clear();
		}
	}
}
