package unknow.kyhtanil.server.system;

import java.io.IOException;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;

import unknow.kyhtanil.common.component.Position;
import unknow.kyhtanil.common.component.Velocity;
import unknow.kyhtanil.common.maps.MapLayout;
import unknow.kyhtanil.server.component.Dirty;
import unknow.kyhtanil.server.manager.LocalizedManager;

/**
 * movement prediction
 * 
 * @author unknow
 */
public class MovementSystem extends IteratingSystem {
	private LocalizedManager locManager;

	private ComponentMapper<Velocity> velocity;
	private ComponentMapper<Position> position;
	private ComponentMapper<Dirty> dirty;

	@Wire
	private MapLayout layout;

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
		double x = p.x + Math.cos(v.direction) * v.speed * world.delta;
		double y = p.y + Math.sin(v.direction) * v.speed * world.delta;
		try {
			if (layout.isWall(x, y))
				return;
		} catch (IOException ex) {
		}
		p.x = (float) x;
		p.y = (float) y;

		locManager.changed(e);
		dirty.get(e).add(p);
	}
}
