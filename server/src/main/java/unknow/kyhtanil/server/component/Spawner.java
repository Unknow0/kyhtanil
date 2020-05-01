package unknow.kyhtanil.server.component;

import com.artemis.PooledComponent;

/**
 * spawn mobs
 * 
 * @author unknow
 */
public class Spawner extends PooledComponent {
	/** center of area to spawn mob */
	public float x;
	/** center of area to spawn mob */
	public float y;
	/** radius of area to spawn mob */
	public float r;

	/** max number of entities to spawn */
	public int max;
	/** actual count of entities */
	public int count;

	/** speed of spawn */
	public float speed;
	/** actual time before spawning one entities */
	public float time;

	/** mob variation that can be spawned */
	public Mob[] mobs;

	@Override
	protected void reset() {
		x = y = r = 0;
		max = count = 0;
		speed = time = 0;
	}

	/**
	 * mob data that can be spawned
	 * 
	 * @author unknow
	 */
	public static class Mob {
		/** name of the mob */
		public String name;
		/** texture to use */
		public String tex;
		/** width of the texture */
		public float w;

		/** load factor for the spawner */
		public int factor;

		/** base stats */
		public int strength;
		/** base stats */
		public int constitution;
		/** base stats */
		public int intelligence;
		/** base stats */
		public int concentration;
		/** base stats */
		public int dexterity;
	}
}
