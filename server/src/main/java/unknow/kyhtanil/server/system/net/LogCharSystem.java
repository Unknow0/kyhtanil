package unknow.kyhtanil.server.system.net;

import org.apache.logging.log4j.*;

import unknow.kyhtanil.common.*;
import unknow.kyhtanil.server.*;
import unknow.kyhtanil.server.component.*;
import unknow.kyhtanil.server.manager.*;

import com.artemis.*;
import com.artemis.systems.*;
import com.artemis.utils.*;

public class LogCharSystem extends IteratingSystem
	{
	private static final Logger log=LogManager.getFormatterLogger(LogCharSystem.class);

	private ComponentMapper<LogChar> logChar;
	private ComponentMapper<NetComp> net;
	private ComponentMapper<StateComp> state;
	private ComponentMapper<PositionComp> position;
	private ComponentMapper<VelocityComp> velocity;
	private ComponentMapper<MobInfoComp> mobInfo;

	private Database database;

	private static final float range=50f;

	public LogCharSystem(Database database)
		{
		super(Aspect.all(LogChar.class, NetComp.class));
		this.database=database;
		}

	protected void process(int e)
		{
		LogChar l=logChar.get(e);
		NetComp ctx=net.get(e);
		if(ctx.channel==null) // entity not finished to be created
			return;

		world.delete(e);
		Integer st=UUIDManager.self().getEntity(l.uuid);
		if(st==null)
			{
			ctx.channel.close();
			log.debug("failed to get State '%s' on log", l.uuid);
			return;
			}
		StateComp s=state.get(st);
		boolean ok=false;
		try
			{
			ok=database.loadPj(s.account.getId(), l.character, st);
			}
		catch (Exception ex)
			{
			log.error("failed to log char", ex);
			}
		if(ok)
			{
			PositionComp p=position.get(st);
			MobInfoComp m=mobInfo.get(st);

			GameServer.world().spawn(s, st);
			PjInfo pjInfo=new PjInfo(m.name, m.level, 1, p.x, p.y, m.hp, m.mp, m.constitution, m.strength, m.concentration, m.intelligence, m.dexterity);
			ctx.channel.write(pjInfo);
			IntBag bag=LocalizedManager.get(p.x, p.y, range);

			for(int i=0; i<bag.size(); i++)
				{
				int em=bag.get(i);
				if(em==st)
					continue;
				UUID uuid=UUIDManager.self().getUuid(em);
				p=position.get(em);
				m=mobInfo.get(em);
				VelocityComp v=velocity.get(em);

				ctx.channel.write(new Spawn(uuid, 0, m.name, 20, p.x, p.y, v.dirX, v.dirY));
				}
			}
		else
			{
			ctx.channel.close();
			}
		ctx.channel.flush();
		}
	}
