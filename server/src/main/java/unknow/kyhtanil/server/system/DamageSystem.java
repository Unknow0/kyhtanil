package unknow.kyhtanil.server.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

import unknow.kyhtanil.common.component.StatShared;
import unknow.kyhtanil.server.component.Damage;
import unknow.kyhtanil.server.component.Dirty;
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

	/**
	 * create new DamageSystem
	 */
	public DamageSystem() {
		super(Aspect.all(Damage.class));
	}

	@Override
	protected void process(int entityId) {
		Damage c = damage.get(entityId);
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