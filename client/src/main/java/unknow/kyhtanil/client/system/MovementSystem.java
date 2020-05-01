package unknow.kyhtanil.client.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

import unknow.kyhtanil.common.component.Position;
import unknow.kyhtanil.common.component.Velocity;

/**
 * Mouvement predition system
 * 
 * @author unknow
 */
public class MovementSystem extends IteratingSystem {
	private ComponentMapper<Velocity> velocity;
	private ComponentMapper<Position> position;

	/**
	 * create new MovementSystem
	 */
	public MovementSystem() {
		super(Aspect.all(Position.class, Velocity.class));
	}

	@Override
	protected void process(int e) {
		Velocity v = velocity.get(e);
		if (v.speed == 0)
			return;

		Position p = position.get(e);
		p.x += Math.cos(v.direction) * v.speed * world.delta;
		p.y += Math.sin(v.direction) * v.speed * world.delta;
	}
}
