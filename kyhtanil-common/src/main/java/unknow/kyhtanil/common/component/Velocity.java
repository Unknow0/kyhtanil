package unknow.kyhtanil.common.component;

/**
 * Component for something that can move
 * 
 * @author unknow
 */
public class Velocity extends Setable<Velocity> {
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
	public void set(Velocity v) {
		this.direction = v.direction;
		this.speed = v.speed;
	}

	@Override
	public String toString() {
		return "Velocity [direction=" + direction + ", speed=" + speed + "]";
	}
}
