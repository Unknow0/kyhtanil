package unknow.kyhtanil.server.component;

import com.artemis.PooledComponent;

import unknow.kyhtanil.server.pojo.IdRate;

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
	public IdRate[] mobs;

	@Override
	protected void reset() {
		x = y = r = 0;
		max = count = 0;
		speed = time = 0;
	}
}
