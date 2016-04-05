package unknow.kyhtanil.server.system.net;

import org.slf4j.*;

import unknow.kyhtanil.common.*;
import unknow.kyhtanil.common.component.*;
import unknow.kyhtanil.common.pojo.*;
import unknow.kyhtanil.server.component.*;
import unknow.kyhtanil.server.manager.*;

import com.artemis.*;
import com.artemis.systems.*;
import com.artemis.utils.*;

public class AttackSystem extends IteratingSystem
	{
	private static final Logger log=LoggerFactory.getLogger(AttackSystem.class);

	private LocalizedManager locManager;
	private UUIDManager manager;
	private ComponentMapper<Attack> attack;
	private ComponentMapper<NetComp> net;
	private ComponentMapper<PositionComp> position;
	private ComponentMapper<DamageListComp> damage;

	public AttackSystem()
		{
		super(Aspect.all(Attack.class, NetComp.class));
		}

	protected void process(int entityId)
		{
		Attack a=attack.get(entityId);
		NetComp ctx=net.get(entityId);
		if(ctx.channel==null) // entity not finished to be created
			return;

		world.delete(entityId);
		Integer e=manager.getEntity(a.uuid);
		if(e==null)
			{
			ctx.channel.close();
			log.debug("failed to get State '{}' on attack", a.uuid);
			return;
			}

		Integer t=null;
		PositionComp sp=position.get(e);
		if(a.target instanceof UUID)
			{
			t=manager.getEntity((UUID)a.target);
			if(t!=null&&sp.distance(position.get(t))>2) // TODO weapon range
				t=null;
			}
		else
			{ // TODO
			IntBag entities=locManager.get(sp.x, sp.y, 2);

			Point p=(Point)a.target;
			double rad1=Math.atan2(p.y-sp.y, p.x-sp.x);
			for(int i=0; i<entities.size(); i++)
				{
				PositionComp m=position.get(entities.get(i));
				double rad2=Math.atan2(m.y-sp.y, m.x-sp.x);

				double dif=rad1-rad2;
				System.out.println(dif);
				if(-.3<dif&&dif<.3)
					t=entities.get(i);
				}
			}
		if(t!=null)
			{
			DamageListComp d=damage.get(t);
			d.add(new DamageListComp.Damage(e, 2, 0, 0, 0, 0, 0, 0, 0f));
//			int damage=10; // TODO
//
//			t.looseHp(damage);
//			send(s, t.x, t.y, new Message(new DamageReport(t.uuid, damage)));
//			if(t.hp()<=0)
//				{
//				// TODO xp & loot
//				despwan(s, t);
//				}
			}
		ctx.channel.flush();
		}
	}
