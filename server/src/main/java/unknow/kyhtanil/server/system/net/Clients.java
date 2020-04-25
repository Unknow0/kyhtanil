package unknow.kyhtanil.server.system.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;

import unknow.kyhtanil.common.component.Position;
import unknow.kyhtanil.common.component.Sprite;
import unknow.kyhtanil.common.component.StatShared;
import unknow.kyhtanil.common.component.Velocity;
import unknow.kyhtanil.common.component.net.Despawn;
import unknow.kyhtanil.common.component.net.Spawn;
import unknow.kyhtanil.common.pojo.UUID;
import unknow.kyhtanil.server.component.StateComp;
import unknow.kyhtanil.server.manager.LocalizedManager;
import unknow.kyhtanil.server.manager.UUIDManager;

public class Clients extends BaseSystem {
	private static final Logger log = LoggerFactory.getLogger(Clients.class);
	public static final float RANGE = 550;

	private LocalizedManager locManager;
	private UUIDManager uuidManager;

	private ComponentMapper<StateComp> state;
	private ComponentMapper<Position> position;
	private ComponentMapper<Velocity> velocity;
	private ComponentMapper<Sprite> sprite;
	private ComponentMapper<StatShared> stat;

	@Override
	protected void processSystem() {
	}

	private final LocalizedManager.Choose filter = new LocalizedManager.Choose() {
		@Override
		public boolean choose(int e) {
			return state.has(e);
		}
	};

	public void send(StateComp sender, float x, float y, Object msg) {
		log.debug("send {} ({}) at {} x {} from {}", msg.getClass().getSimpleName(), msg, x, y, sender);
		IntBag bag = locManager.get(x, y, RANGE, filter);
		for (int i = 0; i < bag.size(); i++) {
			StateComp s = state.get(bag.get(i));
			if (s != sender) {
				log.debug("	{} {}", s.account, uuidManager.getUuid(bag.get(i)));
				s.channel.writeAndFlush(msg);
			}
		}
	}

	public void despawn(StateComp sender, int entityId) {
		Position p = position.get(entityId);
		UUID uuid = uuidManager.getUuid(entityId);
		send(sender, p.x, p.y, new Despawn(uuid));
	}

	public void spawn(StateComp sender, int e) {
		Position p = position.get(e);
		Velocity v = velocity.get(e);
		Sprite s = sprite.get(e);
		StatShared m = stat.get(e);
		UUID uuid = uuidManager.getUuid(e);
		send(sender, p.x, p.y, new Spawn(uuid, s, m, p, v));
	}
}
