package unknow.kyhtanil.server;

import com.artemis.*;

import unknow.kyhtanil.common.component.*;
import unknow.kyhtanil.common.pojo.*;
import unknow.kyhtanil.server.component.*;

public class ApiWorld
	{
	private ComponentMapper<DamageListComp> damage;
	private ComponentMapper<CalculatedComp> calc;

	public void addDamage(int source, Damage dmg, float duration, int target)
		{
		DamageListComp d=damage.get(target);
		d.add(new DamageListComp.Damage(source, 0, r(dmg.slashingMin, dmg.slashingRng), r(dmg.bluntMin, dmg.bluntRng), r(dmg.piercingMin, dmg.piercingRng), r(dmg.lightningMin, dmg.lightningRng), r(dmg.fireMin, dmg.fireRng), r(dmg.iceMin, dmg.iceRng), duration));
		}

	public Damage damage(int source)
		{
		CalculatedComp c=calc.get(source);
		return c.dmg;
		}

	private int r(int min, int rng)
		{
		return min+(int)(Math.random()*rng);
		}
	}
