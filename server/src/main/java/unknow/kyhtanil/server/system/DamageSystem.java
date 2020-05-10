package unknow.kyhtanil.server.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.EntityManager;
import com.artemis.systems.IteratingSystem;

import unknow.kyhtanil.common.component.Dirty;
import unknow.kyhtanil.common.component.StatShared;
import unknow.kyhtanil.server.component.Contribution;
import unknow.kyhtanil.server.component.Damage;
import unknow.kyhtanil.server.component.TTL;

/**
 * apply all damage on entities
 * 
 * @author unknow
 */
public class DamageSystem extends IteratingSystem {
	private ComponentMapper<Damage> damage;
	private ComponentMapper<TTL> ttl;
	private ComponentMapper<StatShared> mobInfo;
	private ComponentMapper<Dirty> dirty;
	private ComponentMapper<Contribution> contib;

	private EntityManager em;

	/**
	 * create new DamageSystem
	 */
	public DamageSystem() {
		super(Aspect.all(Damage.class));
	}

	@Override
	protected void process(int entityId) {
		Damage c = damage.get(entityId);
		if (c.target == -1 || c.source == -1 || !em.isActive(c.target) || !em.isActive(c.source)) {
			world.delete(entityId);
			return;
		}

		Contribution contribution = contib.get(c.target);
		contribution.add(c.source, 1, 30);

		int total = c.base + c.blunt + c.piercing + c.slashing + c.fire + c.ice + c.lightning;

		if (ttl.get(entityId).ttl > 0)
			total *= world.delta;

		StatShared mob = mobInfo.get(c.target);
		mob.hp -= total;
		if (mob.hp <= 0)
			world.delete(c.target);
		else
			dirty.get(c.target).add(mob);

	}
}