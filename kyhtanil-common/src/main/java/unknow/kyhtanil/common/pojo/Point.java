package unknow.kyhtanil.common.pojo;

public class Point {
	public float x;
	public float y;

	public Point() {
	}

	public Point(java.lang.Float x, java.lang.Float y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "Point [x=" + x + ", y=" + y + "]";
	}
}
