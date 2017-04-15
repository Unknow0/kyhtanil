package unknow.kyhtanil.server.system.net;

import javax.script.*;

import org.slf4j.*;

import unknow.kyhtanil.common.*;
import unknow.kyhtanil.common.component.*;
import unknow.kyhtanil.common.pojo.*;
import unknow.kyhtanil.server.*;
import unknow.kyhtanil.server.manager.*;

import com.artemis.*;
import com.artemis.annotations.*;
import com.artemis.systems.*;
import com.artemis.utils.*;
import com.esotericsoftware.kryo.util.*;

public class AttackSystem extends IteratingSystem
	{
	private static final Logger log=LoggerFactory.getLogger(AttackSystem.class);

	private LocalizedManager locManager;
	private UUIDManager manager;
	private ComponentMapper<Attack> attack;
	private ComponentMapper<NetComp> net;
	private ComponentMapper<PositionComp> position;

	@Wire
	private ApiWorld api;
	@Wire
	private ScriptEngine js;
	@Wire
	private Database database;
	private Bindings bind;

	private IntMap<CompiledScript> skills;

	public AttackSystem()
		{
		super(Aspect.all(Attack.class, NetComp.class));
		}

	public void delayedInit()
		{
		bind=js.createBindings();
		bind.put("api", api);
		try
			{
			database.init();
			skills=database.processSkill((Compilable)js);
			}
		catch (Exception e)
			{
			throw new RuntimeException("failled to init skills", e);
			}
		}

	protected void process(int entityId)
		{
		Attack a=attack.get(entityId);
		NetComp ctx=net.get(entityId);
		if(ctx.channel==null) // entity not finished to be created
			return;

		world.delete(entityId);
		Integer self=manager.getEntity(a.uuid);
		if(self==null)
			{
			ctx.channel.close();
			log.debug("failed to get State '{}' on attack", a.uuid);
			return;
			}

		float range=5;
		Integer t=null;
		PositionComp sp=position.get(self);
		if(a.target instanceof UUID)
			{
			t=manager.getEntity((UUID)a.target);
			if(t!=null&&sp.distance(position.get(t))>range) // TODO weapon range
				t=null;
			}
		else
			{ // TODO
			IntBag entities=locManager.get(sp.x, sp.y, range);

			Point p=(Point)a.target;
			double rad1=Math.atan2(p.y-sp.y, p.x-sp.x);
			for(int i=0; i<entities.size(); i++)
				{
				PositionComp m=position.get(entities.get(i));
				double rad2=Math.atan2(m.y-sp.y, m.x-sp.x);

				double dif=rad1-rad2;
				if(-.3<dif&&dif<.3)
					t=entities.get(i);
				}
			}
		if(t!=null)
			{
			try
				{
				bind.put("target", t);
				bind.put("self", self);
				CompiledScript script=skills.get(a.id);
				script.eval(bind);
				}
			catch (ScriptException e)
				{
				log.error("failed to execute skill", e);
				}
//			DamageListComp d=damage.get(t);
//			d.add(new DamageListComp.Damage(e, 2, 0, 0, 0, 0, 0, 0, 0f));
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
