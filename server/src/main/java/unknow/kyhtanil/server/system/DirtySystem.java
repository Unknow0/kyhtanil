package unknow.kyhtanil.server.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

import unknow.kyhtanil.common.component.Dirty;
import unknow.kyhtanil.common.component.Position;
import unknow.kyhtanil.common.component.StatAgg;
import unknow.kyhtanil.common.component.Velocity;
import unknow.kyhtanil.common.component.net.UpdateInfo;
import unknow.kyhtanil.common.pojo.UUID;
import unknow.kyhtanil.server.component.StateComp;
import unknow.kyhtanil.server.manager.UUIDManager;
import unknow.kyhtanil.server.system.net.Clients;

/**
 * send and clear dirty event
 * 
 * @author unknow
 */
public class DirtySystem extends IteratingSystem {
	private ComponentMapper<Dirty> dirty;
	private ComponentMapper<StateComp> state;

	private UUIDManager uuid;
	private Clients clients;

	/**
	 * create new DirtySystem
	 */
	public DirtySystem() {
		super(Aspect.all(Dirty.class, Position.class));
	}

	@Override
	protected void process(int e) {
		Dirty d = dirty.get(e);
		if (d.map.isEmpty())
			return;

		UUID u = uuid.getUuid(e);
		if (u == null)
			return;

		StateComp s = state.get(e);
		if (s != null) {
			UpdateInfo changed = d.changed(u, Position.class, Velocity.class);
			if (changed != null)
				s.channel.writeAndFlush(changed);
		}
		clients.send(e, d.changed(u, StatAgg.class));
		// TODO notify other server
		d.reset();
	}
}
