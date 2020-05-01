/**
 * 
 */
package unknow.kyhtanil.server.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

import unknow.kyhtanil.server.component.Spawned;
import unknow.kyhtanil.server.component.Spawner;

/**
 * manage spawned entities
 * 
 * @author unknow
 */
public class SpawnedSystem extends IteratingSystem {
	private ComponentMapper<Spawned> spawned;
	private ComponentMapper<Spawner> spawner;

	/**
	 * create new SpawnedSystem
	 */
	public SpawnedSystem() {
		super(Aspect.all(Spawned.class));
	}

	@Override
	protected void removed(int entityId) {
		Spawner s = spawner.get(spawned.get(entityId).spawner);
		s.count--;
	}

	@Override
	protected void process(int entityId) {
	}
}
