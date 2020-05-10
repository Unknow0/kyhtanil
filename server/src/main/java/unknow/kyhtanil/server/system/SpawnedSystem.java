/**
 * 
 */
package unknow.kyhtanil.server.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.utils.IntMap.Entry;

import unknow.kyhtanil.common.component.Dirty;
import unknow.kyhtanil.common.component.StatBase;
import unknow.kyhtanil.server.component.Contribution;
import unknow.kyhtanil.server.component.Contribution.D;
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
	private ComponentMapper<Contribution> contrib;
	private ComponentMapper<StatBase> base;
	private ComponentMapper<Dirty> dirty;

	/**
	 * create new SpawnedSystem
	 */
	public SpawnedSystem() {
		super(Aspect.all(Spawned.class));
	}

	@Override
	protected void removed(int entityId) {
		Spawned s = spawned.get(entityId);
		if (s.spawner == -1)
			return;
		spawner.get(s.spawner).count--;

		// TODO give out reward
		Contribution contribution = contrib.get(entityId);
		for (Entry<D> e : contribution.contributions.entries()) {
			StatBase p = base.get(e.key);
			p.xp += 1; // TODO
			dirty.get(e.key).add(p);
		}
	}

	@Override
	protected void process(int entityId) {
	}
}
