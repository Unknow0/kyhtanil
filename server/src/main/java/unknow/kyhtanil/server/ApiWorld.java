package unknow.kyhtanil.server;

import unknow.kyhtanil.common.component.*;
import unknow.kyhtanil.common.pojo.*;
import unknow.kyhtanil.server.component.*;
import unknow.kyhtanil.server.manager.*;
import unknow.kyhtanil.server.system.*;
import unknow.kyhtanil.server.utils.*;

import com.artemis.*;
import com.artemis.utils.*;

public class ApiWorld
	{
	private World world;

	private EventSystem event;
	private LocalizedManager locManager;

	private ComponentMapper<DamageListComp> damage;
	private ComponentMapper<CalculatedComp> calc;
	private ComponentMapper<PositionComp> pos;
	private ComponentMapper<VelocityComp> velocity;
	private ComponentMapper<Projectile> proj;

	public void addDamage(int source, Damage dmg, float duration, int target)
		{
		DamageListComp d=damage.get(target);
		d.add(new DamageListComp.Damage(source, 0, r(dmg.slashingMin, dmg.slashingRng), r(dmg.bluntMin, dmg.bluntRng), r(dmg.piercingMin, dmg.piercingRng), r(dmg.lightningMin, dmg.lightningRng), r(dmg.fireMin, dmg.fireRng), r(dmg.iceMin, dmg.iceRng), duration));
		}

	public void addProj(int source, float dir, float speed, Event onHit)
		{
		final int e=world.create(Archetypes.proj);
		pos.get(e).set(pos.get(source));
		VelocityComp v=velocity.get(e);
		v.direction=dir;
		v.speed=speed;
		Projectile p=proj.get(e);
		p.onHit=onHit;
		p.source=source;
		p.tex="proj.png";

		event.register(source, new EventSystem.EntityListener()
			{
				@Override
				public void inserted(int entityId)
					{
					}

				@Override
				public void removed(int entityId)
					{
					world.delete(e);
					}
			});
		}

	public IntBag getMobs(PositionComp p, float r)
		{
		return locManager.get(p.x, p.y, r);
		}

	public CalculatedComp stat(int source)
		{
		return calc.get(source);
		}

	public PositionComp position(int source)
		{
		return pos.get(source);
		}

	private int r(int min, int rng)
		{
		return min+(int)(Math.random()*rng);
		}
	}
