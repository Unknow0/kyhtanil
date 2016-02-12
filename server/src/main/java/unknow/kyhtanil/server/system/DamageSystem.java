package unknow.kyhtanil.server.system;

import unknow.kyhtanil.common.*;
import unknow.kyhtanil.server.*;
import unknow.kyhtanil.server.component.*;
import unknow.kyhtanil.server.manager.*;

import com.artemis.*;

public class DamageSystem extends CompositeEntityProcessor<DamageListComp,DamageListComp.Damage>
	{
	private ComponentMapper<MobInfoComp> mobInfo;
	private ComponentMapper<PositionComp> position;

	private MobInfoComp mob;
	private PositionComp p;
	private UUID uuid;

	public DamageSystem()
		{
		super(Aspect.all(DamageListComp.class, PositionComp.class, MobInfoComp.class), DamageListComp.class);
		}

	protected MobInfoComp processEntity(int e)
		{
		return mobInfo.get(e);
		}

	protected boolean processStart(int e)
		{
		mob=mobInfo.get(e);
		p=position.get(e);
		uuid=UUIDManager.self().getUuid(e);
		return true;
		}

	protected boolean processComponent(int e, DamageListComp.Damage c)
		{
		// TODO res calculation
		int total=c.base+c.blunt+c.piercing+c.slashing+c.fire+c.ice+c.lightning;

		if(c.duration>0)
			total*=world.delta;

		mob.hp-=total;

		GameServer.world().send(null, p.x, p.y, new DamageReport(uuid, total));
		if(mob.hp<=0)
			{
			world.delete(e);
			return false;
			}
		return true;
		}

	protected void processEnd(int e)
		{
		}
	}
