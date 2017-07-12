package unknow.kyhtanil.server.system;

import unknow.kyhtanil.common.component.*;
import unknow.kyhtanil.common.component.net.*;
import unknow.kyhtanil.common.pojo.*;
import unknow.kyhtanil.server.*;
import unknow.kyhtanil.server.component.*;
import unknow.kyhtanil.server.manager.*;

import com.artemis.*;

public class DamageSystem extends CompositeEntityProcessor<DamageListComp,DamageListComp.Damage>
	{
	private GameWorld gameWorld;
	private UUIDManager manager;
	private ComponentMapper<MobInfoComp> mobInfo;
	private ComponentMapper<PositionComp> position;

	private MobInfoComp mob;
	private PositionComp p;
	private UUID uuid;

	public DamageSystem(GameWorld gameWorld)
		{
		super(Aspect.all(DamageListComp.class, PositionComp.class, MobInfoComp.class), DamageListComp.class);
		this.gameWorld=gameWorld;
		}

	protected MobInfoComp processEntity(int e)
		{
		return mobInfo.get(e);
		}

	protected boolean processStart(int e)
		{
		mob=mobInfo.get(e);
		p=position.get(e);
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

		gameWorld.send(null, p.x, p.y, new DamageReport(uuid, total));
		if(mob.hp<=0)
			{
			gameWorld.send(null, p.x, p.y, new Despawn(uuid));
			world.delete(e);
			return false;
			}
		return true;
		}

	protected void processEnd(int e)
		{
		}
	}
