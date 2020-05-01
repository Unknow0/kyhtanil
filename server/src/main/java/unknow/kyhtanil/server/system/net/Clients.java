package unknow.kyhtanil.server.system.net;

import java.util.function.IntPredicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;

import unknow.kyhtanil.common.component.Position;
import unknow.kyhtanil.common.component.net.Despawn;
import unknow.kyhtanil.common.pojo.UUID;
import unknow.kyhtanil.server.component.StateComp;
import unknow.kyhtanil.server.manager.LocalizedManager;
import unknow.kyhtanil.server.manager.UUIDManager;

/**
 * send stuff to client
 * 
 * @author unknow
 */
public class Clients extends BaseSystem {
	private static final Logger log = LoggerFactory.getLogger(Clients.class);

	/** range of listening client */
	public static final float RANGE = 500;

	private LocalizedManager locManager;
	private UUIDManager uuidManager;

	private ComponentMapper<StateComp> state;
	private ComponentMapper<Position> position;

	@Override
	protected void processSystem() {
	}

	private final IntPredicate filter = e -> state.has(e);

	/**
	 * send message to all client in range
	 * 
	 * @param entityId the source of message
	 * @param msg      the message to send
	 */
	public void send(int entityId, Object msg) {
		Position p = position.get(entityId);
		StateComp sender = state.get(entityId);
		log.debug("send {} ({}) at {} x {} from {}", msg.getClass().getSimpleName(), msg, p.x, p.y, sender);
		IntBag bag = locManager.get(p.x, p.y, RANGE, filter);
		for (int i = 0; i < bag.size(); i++) {
			StateComp s = state.get(bag.get(i));
			if (s != sender) {
				log.debug("	{} {}", s.account, uuidManager.getUuid(bag.get(i)));
				s.channel.writeAndFlush(msg);
			}
		}
	}

	/**
	 * send a despawn event
	 * 
	 * @param entityId entity that despawn
	 */
	public void despawn(int entityId) {
		UUID uuid = uuidManager.getUuid(entityId);
		send(entityId, new Despawn(uuid));
	}
}
