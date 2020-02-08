package unknow.kyhtanil.client.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

import unknow.kyhtanil.common.component.PositionComp;
import unknow.kyhtanil.common.component.VelocityComp;

public class MovementSystem extends IteratingSystem {
	private ComponentMapper<VelocityComp> velocity;
	private ComponentMapper<PositionComp> position;

	public MovementSystem() {
		super(Aspect.all(PositionComp.class, VelocityComp.class));
	}

	@Override
	protected void process(int e) {
		VelocityComp v = velocity.get(e);
		if (v.speed == 0)
			return;

		PositionComp p = position.get(e);
		p.x += Math.cos(v.direction) * v.speed * world.delta;
		p.y += Math.sin(v.direction) * v.speed * world.delta;
	}
}
