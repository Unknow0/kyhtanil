package unknow.kyhtanil.server.component;

import com.artemis.PooledComponent;

public class Spawner extends PooledComponent {
	public float x, y;
	public float r;

	public int max;
	public int count;

	public float speed;
	public float time;

	public Mob[] mobs;

	@Override
	protected void reset() {
		x = y = r = 0;
		max = count = 0;
		speed = time = 0;
	}

	public static class Mob {
		public String name;
		public String tex;
		public float w;

		public int factor;

		public int strength;
		public int constitution;
		public int intelligence;
		public int concentration;
		public int dexterity;
	}
}
