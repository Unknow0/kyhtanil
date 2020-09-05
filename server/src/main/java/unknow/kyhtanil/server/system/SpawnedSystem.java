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

		Inventory mobInventory = inventory.get(entityId);

		Contribution contribution = contrib.get(entityId);
		for (Entry<D> e : contribution.contributions.entries()) {
			Dirty d = dirty.get(e.key);
			StatBase p = base.get(e.key);
			p.xp += 1; // TODO
			d.add(p);

			Inventory inv = inventory.get(e.key);
			if (!mobInventory.items.isEmpty()) {
				Inventory.Add add = new Inventory.Add(); // TODO pool
				add.addAll(mobInventory.items);
				d.add(add);
				inv.items.addAll(mobInventory.items);
			}
		}
	}

	@Override
	protected void process(int entityId) {
	}
}
