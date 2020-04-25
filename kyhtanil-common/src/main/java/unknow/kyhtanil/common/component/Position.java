package unknow.kyhtanil.common.component;

import com.artemis.PooledComponent;

public class Position extends PooledComponent implements Setable<Position> {
	public float x, y;

	@Override
	protected void reset() {
		x = y = 0;
	}

	public double distance(Position p) {
		return distance(p.x, p.y);
	}

	@Override
	public void set(Position p) {
		this.x = p.x;
		this.y = p.y;
	}

	public double distance(float x, float y) {
		float dx = this.x - x;
		float dy = this.y - y;
		return Math.sqrt(dx * dx + dy * dy);
	}

	@Override
	public String toString() {
		return "Position [x=" + x + ", y=" + y + "]";
	}
}
