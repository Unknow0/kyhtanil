package unknow.kyhtanil.common.component;

import com.artemis.PooledComponent;

/**
 * Component for something that can move
 * 
 * @author unknow
 */
public class Velocity extends PooledComponent implements Setable<Velocity> {
	/** the direction in radiant */
	public float direction;
	/** the speed */
	public float speed;

	/**
	 * create new Velocity
	 */
	public Velocity() {
	}

	/**
	 * create new Velocity
	 * 
	 * @param direction
	 * @param speed
	 */
	public Velocity(float direction, float speed) {
		this.direction = direction;
		this.speed = speed;
	}

	@Override
	protected void reset() {
	}

	@Override
	public Class<Velocity> component() {
		return Velocity.class;
	}

	@Override
	public void setTo(Velocity m) {
		m.direction = this.direction;
		m.speed = this.speed;
	}

	@Override
	public String toString() {
		return "Velocity [direction=" + direction + ", speed=" + speed + "]";
	}
}
