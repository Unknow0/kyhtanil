package unknow.kyhtanil.server.system.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

import unknow.kyhtanil.common.component.ErrorComp;
import unknow.kyhtanil.common.component.account.CreateAccount;
import unknow.kyhtanil.common.component.account.LogResult;
import unknow.kyhtanil.common.component.net.NetComp;
import unknow.kyhtanil.common.pojo.CharDesc;
import unknow.kyhtanil.common.pojo.UUID;
import unknow.kyhtanil.server.Database;
import unknow.kyhtanil.server.component.Archetypes;
import unknow.kyhtanil.server.component.StateComp;
import unknow.kyhtanil.server.component.StateComp.States;
import unknow.kyhtanil.server.manager.StateManager;

/**
 * manage the login request <br>
 * assign an UUID to the new State
 */
public class CreateAccountSystem extends IteratingSystem {
	private static final Logger log = LoggerFactory.getLogger(CreateAccountSystem.class);

	private StateManager manager;
	private ComponentMapper<CreateAccount> create;
	private ComponentMapper<NetComp> net;
	private ComponentMapper<StateComp> state;

	private Archetypes arch;
	private Database database;

	public CreateAccountSystem() {
		super(Aspect.all(CreateAccount.class, NetComp.class));
	}

	@Override
	protected void process(int e) {
		CreateAccount l = create.get(e);
		NetComp ctx = net.get(e);

		world.delete(e);
		try {
			Integer a = database.createAccount(l.login, l.passHash);
			if (a != null) {
				int ns = world.create(arch.login);
				StateComp s = state.get(ns);
				s.account = a;
				s.channel = ctx.channel;
				s.state = States.LOGGED;

				UUID uuid = manager.log(ns, a);
				if (uuid != null) {
					log.info("log {} {}", s.account, uuid);
					ctx.channel.writeAndFlush(new LogResult(uuid, new CharDesc[0]));
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
