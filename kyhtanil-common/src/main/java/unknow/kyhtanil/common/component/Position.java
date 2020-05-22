package unknow.kyhtanil.common.component;

import com.artemis.PooledComponent;

/**
 * the position
 * 
 * @author unknow
 */
public class Position extends PooledComponent implements Setable<Position> {
	/** the x position */
	public float x;
	/** the y position */
	public float y;

	/**
	 * create new Position
	 */
	public Position() {
	}

	/**
	 * create new Position
	 * 
	 * @param x
	 * @param y
	 */
	public Position(float x, float y) {
		this.x = x;
		this.y = y;
	}

	@Override
	protected void reset() {
		x = y = 0;
	}

	/**
	 * compute the distance
	 * 
	 * @param p the other position
	 * @return the distance
	 */
	public double distance(Position p) {
		return distance(p.x, p.y);
	}

	/**
	 * compute the distance
	 * 
	 * @param x the x
	 * @param y the y
	 * @return the distance
	 */
	public double distance(float x, float y) {
		float dx = this.x - x;
		float dy = this.y - y;
		return Math.sqrt(dx * dx + dy * dy);
	}

	@Override
	public Class<Position> component() {
		return Position.class;
	}

	@Override
	public void setTo(Position p) {
		p.x = this.x;
		p.y = this.y;
	}

	@Override
	public String toString() {
		return "Position [x=" + x + ", y=" + y + "]";
	}
}
