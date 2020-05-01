package unknow.kyhtanil.client.system.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
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
import unknow.kyhtanil.common.component.Position;
import unknow.kyhtanil.common.component.account.CreateAccount;
import unknow.kyhtanil.common.component.account.CreateChar;
import unknow.kyhtanil.common.component.account.LogChar;
import unknow.kyhtanil.common.component.account.Login;
import unknow.kyhtanil.common.component.net.Attack;
import unknow.kyhtanil.common.component.net.Move;
import unknow.kyhtanil.common.pojo.CharDesc;
import unknow.kyhtanil.common.pojo.UUID;
import unknow.kyhtanil.common.util.KyhtanilSerialize;

/**
 * global connection to the server
 * 
 * @author unknow
 */
public class Connection extends BaseSystem implements Runnable {
	private static final Logger log = LoggerFactory.getLogger(Connection.class);

	private List<Component> list = new ArrayList<>();

	private Thread t;
	private Socket co;
	private InputStream in;
	private OutputStream out;

	private State state;

	/**
	 * create new Connection
	 * 
	 * @param host
	 * @param port
	 * @throws IOException
	 */
	public Connection(String host, int port) throws IOException {
		log.info("connecting to {}:{}", host, port);
		SocketHints hints = new SocketHints();
		hints.keepAlive = true;
		co = Gdx.net.newClientSocket(Protocol.TCP, host, port, hints);

		in = co.getInputStream();
		out = co.getOutputStream();
		KyhtanilSerialize.write(KyhtanilSerialize.hash(), out);
		byte[] h = (byte[]) KyhtanilSerialize.read(in);
		if (!Arrays.equals(h, KyhtanilSerialize.hash()))
			throw new IOException("Invalide version");

		t = new Thread(this);
		t.setDaemon(true);
		t.start();
	}

	/**
	 * create an account
	 * 
	 * @param login the login
	 * @param pass  the pass
	 */
	public void createAccount(String login, String pass) {
		write(new CreateAccount(login, hash(login, pass)));
	}

	/**
	 * create a caracter
	 * 
	 * @param name the name
	 */
	public void createChar(String name) {
		write(new CreateChar(state.uuid, name));
	}

	/**
	 * login to the game
	 * 
	 * @param login the login
	 * @param pass  the pass
	 */
	public void login(String login, String pass) {
		write(new Login(login, hash(login, pass)));
	}

	private static byte[] hash(String login, String pass) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			md.update(login.toLowerCase().getBytes(StandardCharsets.UTF_8));
			md.update((byte) ':');
			md.update(pass.getBytes(StandardCharsets.UTF_8));
			return md.digest();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * send the updated position
	 * 
	 * @param id        the uuid
	 * @param p         the new position
	 * @param direction the direction
	 */
	public void update(UUID id, Position p, float direction) {
		write(new Move(id, p.x, p.y, direction));
	}

	/**
	 * log a char to the game
	 * 
	 * @param uuid     the uuid
	 * @param charDesc the char to log
	 */
	public void logChar(UUID uuid, CharDesc charDesc) {
		write(new LogChar(uuid, charDesc.id));
	}

	/**
	 * send an attack
	 * 
	 * @param uuid   the uuid
	 * @param attId  the id of the attack
	 * @param target the target uuid (can be null if no selected target)
	 * @param x      the target x
	 * @param y      the target y
	 */
	public void attack(UUID uuid, int attId, UUID target, float x, float y) {
		write(new Attack(uuid, attId, target == null ? new Position(x, y) : target));
	}

	/**
	 * write an object to the server
	 * 
	 * @param o object to write
	 * @throws IOException
	 */
	private void write(Object o) {
		try {
			log.info("write {}", o);
			KyhtanilSerialize.write(o, out);
		} catch (IOException e) {
			log.error("failed to send", e);
			// TODO manage error
		}
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
