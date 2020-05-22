/**
 * 
 */
package unknow.kyhtanil.server.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.utils.IntMap.Entry;

import unknow.kyhtanil.common.component.Dirty;
import unknow.kyhtanil.common.component.Inventory;
import unknow.kyhtanil.common.component.StatBase;
import unknow.kyhtanil.server.component.Contribution;
import unknow.kyhtanil.server.component.Contribution.D;
import unknow.kyhtanil.server.component.Spawned;
import unknow.kyhtanil.server.component.Spawner;
import unknow.kyhtanil.server.pojo.IdRate;

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
	private ComponentMapper<Inventory> inventory;
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

		IdRate[] loots = s.mob.loots;
		int len = loots.length;

		Contribution contribution = contrib.get(entityId);
		for (Entry<D> e : contribution.contributions.entries()) {
			Dirty d = dirty.get(e.key);
			StatBase p = base.get(e.key);
			p.xp += 1; // TODO
			d.add(p);

			double random = Math.random();
			int i = 0;
			IdRate r;

			Inventory inv = inventory.get(e.key);
			Inventory.Add add = new Inventory.Add(); // TODO pool
			while (i < len && (r = loots[i++]).rate < random) {
				inv.items.add(r.id);
				add.add(r.id);
			}
			if (!add.isEmpty())
				d.add(add);
		}
	}

	@Override
	protected void process(int entityId) {
	}
}
