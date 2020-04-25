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
import unknow.kyhtanil.common.component.Position;
import unknow.kyhtanil.common.component.StatAgg;
import unknow.kyhtanil.common.component.Velocity;
import unknow.kyhtanil.common.component.net.Move;
import unknow.kyhtanil.common.component.net.NetComp;
import unknow.kyhtanil.common.maps.MapLayout;
import unknow.kyhtanil.server.component.Dirty;
import unknow.kyhtanil.server.manager.LocalizedManager;
import unknow.kyhtanil.server.manager.UUIDManager;

public class MoveSystem extends IteratingSystem {
	private static final Logger log = LoggerFactory.getLogger(MoveSystem.class);

	private UUIDManager manager;
	private ComponentMapper<Move> move;
	private ComponentMapper<NetComp> net;
	private ComponentMapper<Position> position;
	private ComponentMapper<Velocity> velocity;
	private ComponentMapper<StatAgg> calculated;
	private ComponentMapper<Dirty> dirty;

	private LocalizedManager locManager;

	@Wire
	private MapLayout layout;

	public MoveSystem() {
		super(Aspect.all(Move.class, NetComp.class));
	}

	@Override
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
		Dirty d = dirty.get(e);

		int moveSpeed = calculated.get(e).get(Stats.MOVE_SPEED);
		Position p = position.get(e);
		try {
			if (p.distance(m.x, m.y) > moveSpeed || layout.isWall(m.x, m.y)) {
				m = new Move(m.uuid, p.x, p.y, m.direction);
				d.add(p);
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (p.x != m.x || p.y != m.y)
			d.add(p);
		p.x = m.x;
		p.y = m.y;
		locManager.changed(e);
		Velocity v = velocity.get(e);
		if (v.direction != m.direction)
			d.add(v);
		v.direction = m.direction;
	}
}
