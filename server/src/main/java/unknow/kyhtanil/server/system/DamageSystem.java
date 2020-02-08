package unknow.kyhtanil.server.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;

import unknow.kyhtanil.common.component.StatShared;
import unknow.kyhtanil.server.component.DamageListComp;
import unknow.kyhtanil.server.component.Dirty;

public class DamageSystem extends CompositeEntityProcessor<DamageListComp, DamageListComp.Damage> {
	private ComponentMapper<StatShared> mobInfo;
	private ComponentMapper<Dirty> dirty;

	private StatShared mob;
	private Dirty d;

	public DamageSystem() {
		super(Aspect.all(DamageListComp.class, StatShared.class), DamageListComp.class);
	}

	protected StatShared processEntity(int e) {
		return mobInfo.get(e);
	}

	protected boolean processStart(int e) {
		mob = mobInfo.get(e);
		d = dirty.get(e);
		return true;
	}

	@Override
	protected boolean processComponent(int e, DamageListComp.Damage c) {
		// TODO res calculation
		int total = c.base + c.blunt + c.piercing + c.slashing + c.fire + c.ice + c.lightning;

		if (c.duration > 0)
			total *= world.delta;

		mob.hp -= total;
		if (mob.hp <= 0) {
			world.delete(e);
			return false;
		}
		d.add(mob);
		return true;
	}

	protected void processEnd(int e) {
	}
}