package unknow.kyhtanil.server.system.net;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;

import io.netty.channel.Channel;
import unknow.kyhtanil.common.Stats;
import unknow.kyhtanil.common.component.ErrorComp;
import unknow.kyhtanil.common.component.PositionComp;
import unknow.kyhtanil.common.component.StatAgg;
import unknow.kyhtanil.common.component.VelocityComp;
import unknow.kyhtanil.common.component.net.Move;
import unknow.kyhtanil.common.component.net.NetComp;
import unknow.kyhtanil.common.maps.MapLayout;
import unknow.kyhtanil.server.GameWorld;
import unknow.kyhtanil.server.component.Dirty;
import unknow.kyhtanil.server.manager.LocalizedManager;
import unknow.kyhtanil.server.manager.UUIDManager;

public class MoveSystem extends IteratingSystem {
	private static final Logger log = LoggerFactory.getLogger(MoveSystem.class);

	private UUIDManager manager;
	private ComponentMapper<Move> move;
	private ComponentMapper<NetComp> net;
	private ComponentMapper<PositionComp> position;
	private ComponentMapper<VelocityComp> velocity;
	private ComponentMapper<StatAgg> calculated;
	private ComponentMapper<Dirty> dirty;

	private LocalizedManager locManager;

	@Wire
	private MapLayout layout;

	public MoveSystem(GameWorld gameWorld) {
		super(Aspect.all(Move.class, NetComp.class));
	}

	protected void process(int entityId) {
		Move m = move.get(entityId);
		NetComp ctx = net.get(entityId);
		Channel chan = ctx.channel;

		world.delete(entityId);
		Integer e = manager.getEntity(m.uuid);
		if (e == null) {
			log.debug("failed to get State '{}' on move", m.uuid);
			chan.writeAndFlush(ErrorComp.INVALID_UUID);
			chan.close();
			return;
		}

		int moveSpeed = calculated.get(e).get(Stats.MOVE_SPEED);
		PositionComp p = position.get(e);
		try {
			if (layout.isWall((int) (m.x / 4), (int) (m.y / 4)) || p.distance(m.x, m.y) > moveSpeed / 100f + .1) // TODO
			{
				m = new Move(m.uuid, p.x, p.y, m.direction);
				chan.writeAndFlush(m);
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Dirty d = dirty.get(e);
		if (p.x != m.x || p.y != m.y)
			d.add(p);
		p.x = m.x;
		p.y = m.y;
		locManager.changed(e);
		VelocityComp v = velocity.get(e);
		if (v.direction != m.direction)
			d.add(v);
		v.direction = m.direction;
	}
}
