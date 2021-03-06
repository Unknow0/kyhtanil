package unknow.kyhtanil.server.system.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

import io.netty.channel.Channel;
import unknow.kyhtanil.common.component.ErrorComp;
import unknow.kyhtanil.common.component.Inventory;
import unknow.kyhtanil.common.component.Position;
import unknow.kyhtanil.common.component.Sprite;
import unknow.kyhtanil.common.component.StatBase;
import unknow.kyhtanil.common.component.StatShared;
import unknow.kyhtanil.common.component.Velocity;
import unknow.kyhtanil.common.component.account.LogChar;
import unknow.kyhtanil.common.component.account.PjInfo;
import unknow.kyhtanil.common.component.net.Despawn;
import unknow.kyhtanil.common.component.net.Spawn;
import unknow.kyhtanil.common.pojo.UUID;
import unknow.kyhtanil.server.Database;
import unknow.kyhtanil.server.component.Archetypes;
import unknow.kyhtanil.server.component.NetComp;
import unknow.kyhtanil.server.component.StateComp;
import unknow.kyhtanil.server.component.StateComp.States;
import unknow.kyhtanil.server.manager.LocalizedManager;
import unknow.kyhtanil.server.manager.LocalizedManager.AreaListener;
import unknow.kyhtanil.server.manager.UUIDManager;
import unknow.kyhtanil.server.system.UpdateStatSystem;

/**
 * Manage the LogChar request <br>
 * Assign the char entity to the State uuid.
 */
public class LogCharSystem extends IteratingSystem {
	private static final Logger log = LoggerFactory.getLogger(LogCharSystem.class);

	private Database database;
	private LocalizedManager locManager;
	private UUIDManager manager;
	private Archetypes arch;

	private ComponentMapper<LogChar> logChar;
	private ComponentMapper<NetComp> net;
	private ComponentMapper<StateComp> state;
	private ComponentMapper<Position> position;
	private ComponentMapper<Sprite> sprite;
	private ComponentMapper<Velocity> velocity;
	private ComponentMapper<StatShared> stats;
	private ComponentMapper<StatBase> perso;
	private ComponentMapper<Inventory> inventory;

	private UpdateStatSystem update;

	/**
	 * create new LogCharSystem
	 */
	public LogCharSystem() {
		super(Aspect.all(LogChar.class, NetComp.class));
	}

	@Override
	protected void process(int e) {
		LogChar l = logChar.get(e);
		NetComp ctx = net.get(e);
		Channel chan = ctx.channel;

		world.delete(e);
		Integer login = manager.getEntity(l.uuid);
		if (login == null) {
			chan.writeAndFlush(ErrorComp.INVALID_UUID);
			chan.close();
			log.debug("failed to get State '{}' on log", l.uuid);
			return;
		}
		StateComp s = state.get(login);
		if (s == null) {
			chan.writeAndFlush(ErrorComp.INVALID_STATE);
			chan.close();
			return;
		}

		int st = world.create(arch.pj);
		UUID uuid = manager.getUuid(login);
		manager.setUuid(login, null);
		manager.setUuid(st, uuid);
		world.delete(login);
		state.get(st).set(s);
		s = state.get(st);

		boolean ok = false;
		try {
			ok = database.loadPj(s.account, l.character, st);
		} catch (Exception ex) {
			log.error("failed to log char", ex);
		}
		if (!ok) {
			chan.writeAndFlush(ErrorComp.UNKNOWN_ERROR);
			chan.close();
			return;
		}

		s.state = States.IN_GAME;

		Sprite sp = sprite.get(st);
		sp.w = 16;
		Position p = position.get(st);
		StatShared m = stats.get(st);
		log.info("log char {} {}", l.uuid, m.name);

		// update calculated values
		update.process(st);

		// spawn the new pj in the world
		Inventory i = inventory.get(st);
		Inventory.Add add = new Inventory.Add();
		add.addAll(i.items);

		PjInfo pjInfo = new PjInfo(p, m, perso.get(st), add);
		s.channel.writeAndFlush(pjInfo);

		locManager.track(st, Clients.RANGE, new SpawnListener(s.channel));
	}

	private class SpawnListener implements AreaListener {
		private Channel chan;

		public SpawnListener(Channel chan) {
			this.chan = chan;
		}

		@Override
		public void enter(int target) {
			UUID uuid = manager.getUuid(target);
			Position p = position.get(target);
			StatShared m = stats.get(target);
			Velocity v = velocity.get(target);
			chan.writeAndFlush(new Spawn(uuid, sprite.get(target), m, p, v));
		}

		@Override
		public void leave(int target) {
			UUID uuid = manager.getUuid(target);
			if (uuid != null)
				chan.writeAndFlush(new Despawn(uuid));
		}
	}
}
