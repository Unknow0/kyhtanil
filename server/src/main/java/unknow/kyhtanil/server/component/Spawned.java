/**
 * 
 */
package unknow.kyhtanil.server.component;

import com.artemis.PooledComponent;

/**
 * entity that got spawned
 * 
 * @author unknow
 */
public class Spawned extends PooledComponent {
	/** spawner entity */
	public int spawner;

	@Override
	protected void reset() {
		spawner = -1;
	}
}
