package unknow.kyhtanil.server.system.net;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

import unknow.kyhtanil.common.component.ErrorComp;
import unknow.kyhtanil.common.component.account.LogResult;
import unknow.kyhtanil.common.component.account.Login;
import unknow.kyhtanil.common.pojo.CharDesc;
import unknow.kyhtanil.common.pojo.UUID;
import unknow.kyhtanil.server.Database;
import unknow.kyhtanil.server.component.Archetypes;
import unknow.kyhtanil.server.component.NetComp;
import unknow.kyhtanil.server.component.StateComp;
import unknow.kyhtanil.server.component.StateComp.States;
import unknow.kyhtanil.server.manager.UUIDManager;

/**
 * manage the login request <br>
 * assign an UUID to the new State
 */
public class LoginSystem extends IteratingSystem {
	private static final Logger log = LoggerFactory.getLogger(LoginSystem.class);

	private UUIDManager manager;
	private Database database;
	private Archetypes arch;

	private ComponentMapper<Login> login;
	private ComponentMapper<NetComp> net;
	private ComponentMapper<StateComp> state;

	/**
	 * create new LoginSystem
	 */
	public LoginSystem() {
		super(Aspect.all(Login.class, NetComp.class));
	}

	@Override
	protected void process(int e) {
		Login l = login.get(e);
		NetComp ctx = net.get(e);

		world.delete(e);
		try {
			Integer a = database.getAccount(l.login, l.passHash);
			if (a != null) {
				int ns = world.create(arch.login);
				log.info("login {}: {}", l.login, ns);
				StateComp s = state.get(ns);
				s.account = a;
				s.channel = ctx.channel;
				s.state = States.LOGGED;

				UUID uuid = manager.assignUuid(ns);
				if (uuid != null) {
					List<CharDesc> charList = database.getCharList(a);
					log.info("log {} {}", s.account, uuid);
					ctx.channel.writeAndFlush(new LogResult(uuid, charList.toArray(new CharDesc[0])));
				} else
					ctx.channel.writeAndFlush(ErrorComp.ALREADY_LOGGED);
			} else
				ctx.channel.writeAndFlush(ErrorComp.INVALID_LOGIN);
		} catch (Exception ex) {
			log.error("failed to log char", ex);
			ctx.channel.writeAndFlush(ErrorComp.UNKNOWN_ERROR);
		}
	}
}
