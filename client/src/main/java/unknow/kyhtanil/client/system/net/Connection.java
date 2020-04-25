package unknow.kyhtanil.client.system.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

import unknow.kyhtanil.client.system.State;
import unknow.kyhtanil.common.component.account.CreateAccount;
import unknow.kyhtanil.common.component.account.CreateChar;
import unknow.kyhtanil.common.component.account.LogChar;
import unknow.kyhtanil.common.component.account.Login;
import unknow.kyhtanil.common.component.net.Attack;
import unknow.kyhtanil.common.component.net.Move;
import unknow.kyhtanil.common.pojo.CharDesc;
import unknow.kyhtanil.common.pojo.Point;
import unknow.kyhtanil.common.pojo.UUID;
import unknow.kyhtanil.common.util.KyhtanilSerialize;

public class Connection extends BaseSystem implements Runnable {
	private static final Logger log = LoggerFactory.getLogger(Connection.class);

	private List<Component> list = new ArrayList<>();

	private Thread t;
	private Socket co;
	private InputStream in;
	private OutputStream out;

	private State state;

	public Connection(String host, int port) throws Exception {
		log.info("connecting to {}:{}", host, port);
		SocketHints hints = new SocketHints();
		hints.keepAlive = true;
		co = Gdx.net.newClientSocket(Protocol.TCP, host, port, hints);

		in = co.getInputStream();
		out = co.getOutputStream();
		KyhtanilSerialize.write(KyhtanilSerialize.hash(), out);
		byte[] h = (byte[]) KyhtanilSerialize.read(in);
		if (!Arrays.equals(h, KyhtanilSerialize.hash()))
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
		write(new CreateAccount(login, md.digest()));
	}

	public void createChar(String name) throws IOException {
		write(new CreateChar(state.uuid, name));
	}

	public void login(String login, String pass) throws IOException, NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-512");
		md.update(login.toLowerCase().getBytes("UTF8"));
		md.update((byte) ':');
		md.update(pass.getBytes("UTF8"));
		write(new Login(login, md.digest()));
	}

	public void update(UUID id, float x, float y, float direction) throws IOException {
		write(new Move(id, x, y, direction));
	}

	public void logChar(UUID uuid, CharDesc charDesc) throws IOException {
		write(new LogChar(uuid, charDesc.id));
	}

	public void attack(UUID uuid, int attId, UUID target, float x, float y) throws IOException {
		write(new Attack(uuid, attId, target == null ? new Point(x, y) : target));
	}

	private void write(Object o) throws IOException {
		log.info("write {}", o);
		KyhtanilSerialize.write(o, out);
	}

	@Override
	public void run() {
		try {
			while (true) {
				Object o = KyhtanilSerialize.read(in);

				log.info("read {}", o);
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
