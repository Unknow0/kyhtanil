package unknow.kyhtanil.server.system.net;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

import io.netty.channel.Channel;
import unknow.kyhtanil.common.component.ErrorComp;
import unknow.kyhtanil.common.component.account.CreateChar;
import unknow.kyhtanil.common.component.account.LogResult;
import unknow.kyhtanil.common.component.net.NetComp;
import unknow.kyhtanil.common.pojo.CharDesc;
import unknow.kyhtanil.server.Database;
import unknow.kyhtanil.server.component.StateComp;
import unknow.kyhtanil.server.manager.UUIDManager;

/**
 * manage the login request <br>
 * assign an UUID to the new State
 */
public class CreateCharSystem extends IteratingSystem {
	private static final Logger log = LoggerFactory.getLogger(CreateCharSystem.class);

	private UUIDManager manager;
	private ComponentMapper<CreateChar> create;
	private ComponentMapper<NetComp> net;
	private ComponentMapper<StateComp> state;

	private Database database;

	public CreateCharSystem() {
		super(Aspect.all(CreateChar.class, NetComp.class));
	}

	@Override
	protected void process(int e) {
		CreateChar l = create.get(e);
		NetComp ctx = net.get(e);
		Channel chan = ctx.channel;

		world.delete(e);
		Integer a = manager.getEntity(l.uuid);
		if (a == null) {
			chan.writeAndFlush(ErrorComp.INVALID_UUID);
			chan.close();
			log.debug("failed to get State '{}' on log", l.uuid);
			return;
		}
		StateComp s = state.get(a);
		try {
			database.createChar(s.account, l.name);
			List<CharDesc> charList = database.getCharList(s.account);
			ctx.channel.writeAndFlush(new LogResult(l.uuid, charList.toArray(new CharDesc[0])));
		} catch (Exception ex) {
			log.error("failed to log char", ex);
			ctx.channel.writeAndFlush(ErrorComp.NAME_ALREADY_USED);
		}
	}
}
