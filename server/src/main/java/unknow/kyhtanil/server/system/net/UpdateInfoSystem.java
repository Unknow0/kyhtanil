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
import unknow.kyhtanil.common.component.Dirty;
import unknow.kyhtanil.common.component.ErrorComp;
import unknow.kyhtanil.common.component.Position;
import unknow.kyhtanil.common.component.Setable;
import unknow.kyhtanil.common.component.StatAgg;
import unknow.kyhtanil.common.component.StatBase;
import unknow.kyhtanil.common.component.Velocity;
import unknow.kyhtanil.common.component.net.UpdateInfo;
import unknow.kyhtanil.common.maps.MapLayout;
import unknow.kyhtanil.server.component.NetComp;
import unknow.kyhtanil.server.manager.LocalizedManager;
import unknow.kyhtanil.server.manager.UUIDManager;

/**
 * handle move event from client
 * 
 * @author unknow
 */
public class UpdateInfoSystem extends IteratingSystem {
	private static final Logger log = LoggerFactory.getLogger(UpdateInfoSystem.class);

	private UUIDManager manager;
	private ComponentMapper<NetComp> net;
	private ComponentMapper<UpdateInfo> info;
	private ComponentMapper<Position> position;
	private ComponentMapper<Velocity> velocity;
	private ComponentMapper<StatAgg> calculated;
	private ComponentMapper<StatBase> base;
	private ComponentMapper<Dirty> dirty;

	private LocalizedManager locManager;

	@Wire
	private MapLayout layout;

	/**
	 * create new MoveSystem
	 */
	public UpdateInfoSystem() {
		super(Aspect.all(UpdateInfo.class, NetComp.class));
	}

	@Override
	protected void process(int entityId) {
		NetComp ctx = net.get(entityId);
		Channel chan = ctx.channel;
		UpdateInfo u = info.get(entityId);

		world.delete(entityId);
		Integer e = manager.getEntity(u.uuid);
		if (e == null) {
			log.debug("failed to get State '{}' on move", u.uuid);
			chan.writeAndFlush(ErrorComp.INVALID_UUID);
			chan.close();
			return;
		}
		Dirty d = dirty.get(e);

		// TODO validate if from client else apply
		for (Setable<?> c : u.c) {
			if (c instanceof Position)
				update(chan, d, e, (Position) c);
			else if (c instanceof Velocity)
				update(d, e, (Velocity) c);
			else if (c instanceof StatBase)
				update(d, e, (StatBase) c);
			else
				log.error("invalid component");
		}
	}

	private void update(Channel chan, Dirty d, int e, Position newPos) {
		int moveSpeed = calculated.get(e).get(Stats.MOVE_SPEED);
		Position p = position.get(e);
		try {
			// allow .1sec of movement error
			if (p.distance(newPos.x, newPos.y) > moveSpeed * .1 || layout.isWall(newPos.x, newPos.y)) {
				d.add(p);
				chan.writeAndFlush(new UpdateInfo(manager.getUuid(e), p));
				return;
			}
		} catch (IOException e1) {
			log.error("failed to load map", e1);
		}
		if (p.x != newPos.x || p.y != newPos.y)
			d.add(p);
		newPos.setTo(p);
		locManager.changed(e);
	}

	private void update(Dirty d, int e, Velocity newV) {
		Velocity v = velocity.get(e);
		if (v.direction != newV.direction)
			d.add(v);
		newV.setTo(v);
	}

	private void update(Dirty d, int e, StatBase newBase) {
		StatBase b = base.get(e);
		// disallow lowering level
		if (b.strength > newBase.strength || b.constitution > newBase.constitution || b.intelligence > newBase.intelligence || b.concentration > newBase.concentration || b.dexterity > newBase.dexterity) {
			d.add(b); // revert change
			return;
		}
		int l = b.level + newBase.strength - b.strength + newBase.constitution - b.constitution + newBase.intelligence - b.intelligence + newBase.concentration - b.concentration + newBase.dexterity - b.dexterity;
		int xp = b.xp - StatBase.costdiff(b.level, l);
		// wrong xp value
		if (xp < 0) {
			d.add(b);
			return;
		}
		newBase.setTo(b);
		b.level = l;
		b.xp = xp;
		if (newBase.level != l || newBase.xp != xp)
			d.add(b);
	}
}
