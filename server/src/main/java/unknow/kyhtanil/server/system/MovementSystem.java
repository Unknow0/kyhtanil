package unknow.kyhtanil.server.system;

import java.io.IOException;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;

import unknow.kyhtanil.common.component.PositionComp;
import unknow.kyhtanil.common.component.VelocityComp;
import unknow.kyhtanil.common.component.net.Move;
import unknow.kyhtanil.common.maps.MapLayout;
import unknow.kyhtanil.common.pojo.UUID;
import unknow.kyhtanil.server.manager.LocalizedManager;
import unknow.kyhtanil.server.manager.UUIDManager;
import unknow.kyhtanil.server.system.net.Clients;

public class MovementSystem extends IteratingSystem {
	private UUIDManager uuidManager;
	private LocalizedManager locManager;
	private ComponentMapper<VelocityComp> velocity;
	private ComponentMapper<PositionComp> position;

	private Clients clients;

	@Wire
	private MapLayout layout;

	public MovementSystem() {
		super(Aspect.all(PositionComp.class, VelocityComp.class));
	}

	protected void initialize() {
	}

	private float f = 0;

	@Override
	protected void process(int e) {
		VelocityComp v = velocity.get(e);
		if (v.speed == 0)
			return;

		PositionComp p = position.get(e);
		double x = p.x + Math.cos(v.direction) * v.speed * world.delta;
		double y = p.y + Math.sin(v.direction) * v.speed * world.delta;
		try {
			if (layout.isWall(x, y))
				return;
		} catch (IOException ex) {
		}
		p.x=(float) x;
		p.y=(float) y;

		locManager.changed(e);
		// notify move ?

		UUID uuid = uuidManager.getUuid(e);
		f += world.delta;
		if (f >= 100) // TODO update to avoid flooding event to client
		{
			f -= 100;
			clients.send(null, p.x, p.y, new Move(uuid, p.x, p.y, v.direction));
		}
	}
}
