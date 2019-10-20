package unknow.kyhtanil.server.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;

import unknow.kyhtanil.common.component.PositionComp;
import unknow.kyhtanil.common.component.StatShared;
import unknow.kyhtanil.common.pojo.UUID;
import unknow.kyhtanil.server.component.DamageListComp;
import unknow.kyhtanil.server.component.Dirty;
import unknow.kyhtanil.server.manager.UUIDManager;
import unknow.kyhtanil.server.system.net.Clients;

public class DamageSystem extends CompositeEntityProcessor<DamageListComp,DamageListComp.Damage>
	{
	private Clients clients;
	private UUIDManager manager;
	private ComponentMapper<StatShared> mobInfo;
	private ComponentMapper<PositionComp> position;
	private ComponentMapper<Dirty> dirty;

	private StatShared mob;
	private PositionComp p;
	private Dirty d;
	private UUID uuid;

	public DamageSystem()
		{
		super(Aspect.all(DamageListComp.class, PositionComp.class, StatShared.class), DamageListComp.class);
		}

	protected StatShared processEntity(int e)
		{
		return mobInfo.get(e);
		}

	protected boolean processStart(int e)
		{
		mob=mobInfo.get(e);
		p=position.get(e);
		d=dirty.get(e);
		uuid=manager.getUuid(e);
		return true;
		}

	@Override
	protected boolean processComponent(int e, DamageListComp.Damage c)
		{
		// TODO res calculation
		int total=c.base+c.blunt+c.piercing+c.slashing+c.fire+c.ice+c.lightning;

		if(c.duration>0)
			total*=world.delta;

		mob.hp-=total;
		if(mob.hp<=0)
			{
			world.delete(e);
			return false;
			}
		d.add(mob);
		return true;
		}

	protected void processEnd(int e)
		{
		}
	}