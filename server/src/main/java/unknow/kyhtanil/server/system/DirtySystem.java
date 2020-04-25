package unknow.kyhtanil.server.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

import unknow.kyhtanil.common.component.Position;
import unknow.kyhtanil.common.component.StatAgg;
import unknow.kyhtanil.common.component.Velocity;
import unknow.kyhtanil.common.component.net.UpdateInfo;
import unknow.kyhtanil.common.pojo.UUID;
import unknow.kyhtanil.server.component.Dirty;
import unknow.kyhtanil.server.component.StateComp;
import unknow.kyhtanil.server.manager.UUIDManager;
import unknow.kyhtanil.server.system.net.Clients;

public class DirtySystem extends IteratingSystem {
	private ComponentMapper<Dirty> dirty;
	private ComponentMapper<Position> pos;
	private ComponentMapper<StateComp> state;

	private UUIDManager uuid;
	private Clients clients;

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
		if (s != null)
			s.channel.writeAndFlush(new UpdateInfo(u, d.changed(Velocity.class)));
		Position p = pos.get(e);
		clients.send(s, p.x, p.y, new UpdateInfo(u, d.changed(StatAgg.class)));
		d.reset();
	}
}
