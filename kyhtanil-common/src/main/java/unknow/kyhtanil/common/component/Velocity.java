package unknow.kyhtanil.common.component;

import com.artemis.*;

public class Velocity extends PooledComponent implements Setable<Velocity> {
	public float direction;
	public float speed;

	public Velocity() {
	}

	public Velocity(float direction, float speed) {
		this.direction = direction;
		this.speed = speed;
	}

	@Override
	protected void reset() {
	}

	@Override
	public void set(Velocity v) {
		this.direction = v.direction;
		this.speed = v.speed;
	}

	@Override
	public String toString() {
		return "VelocityComp [direction=" + direction + ", speed=" + speed + "]";
	}
}
