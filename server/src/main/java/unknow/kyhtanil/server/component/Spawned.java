/**
 * 
 */
package unknow.kyhtanil.server.component;

import com.artemis.PooledComponent;

import unknow.kyhtanil.server.pojo.Mob;

/**
 * entity that got spawned
 * 
 * @author unknow
 */
public class Spawned extends PooledComponent {
	/** spawner entity */
	public int spawner;
	/** the spawned mob */
	public Mob mob;

	@Override
	protected void reset() {
		spawner = -1;
	}
}
