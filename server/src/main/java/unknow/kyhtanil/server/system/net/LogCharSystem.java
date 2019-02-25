package unknow.kyhtanil.server.system.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.artemis.utils.IntBag;

import io.netty.channel.Channel;
import unknow.kyhtanil.common.component.Body;
import unknow.kyhtanil.common.component.CalculatedComp;
import unknow.kyhtanil.common.component.ErrorComp;
import unknow.kyhtanil.common.component.MobInfoComp;
import unknow.kyhtanil.common.component.PositionComp;
import unknow.kyhtanil.common.component.SpriteComp;
import unknow.kyhtanil.common.component.VelocityComp;
import unknow.kyhtanil.common.component.account.LogChar;
import unknow.kyhtanil.common.component.account.PjInfo;
import unknow.kyhtanil.common.component.net.NetComp;
import unknow.kyhtanil.common.component.net.Spawn;
import unknow.kyhtanil.common.pojo.UUID;
import unknow.kyhtanil.server.Database;
import unknow.kyhtanil.server.GameWorld;
import unknow.kyhtanil.server.component.StateComp;
import unknow.kyhtanil.server.component.StateComp.States;
import unknow.kyhtanil.server.manager.LocalizedManager;
import unknow.kyhtanil.server.manager.UUIDManager;
import unknow.kyhtanil.server.system.UpdateStatSystem;

/**
 * Manage the LogChar request
 * <br>Assign the char entity to the State uuid.
 */
public class LogCharSystem extends IteratingSystem
	{
	private static final Logger log=LoggerFactory.getLogger(LogCharSystem.class);

	private GameWorld gameWorld;

	private LocalizedManager locManager;
	private UUIDManager manager;

	private ComponentMapper<LogChar> logChar;
	private ComponentMapper<NetComp> net;
	private ComponentMapper<StateComp> state;
	private ComponentMapper<PositionComp> position;
	private ComponentMapper<SpriteComp> sprite;
	private ComponentMapper<VelocityComp> velocity;
	private ComponentMapper<MobInfoComp> mobInfo;
	private ComponentMapper<Body> body;
	private ComponentMapper<CalculatedComp> calculated;

	private UpdateStatSystem update;

	private Database database;

	private static final float range=50f;

	public LogCharSystem(Database database, GameWorld gameWorld)
		{
		super(Aspect.all(LogChar.class, NetComp.class));
		this.database=database;
		this.gameWorld=gameWorld;
		}

	@Override
	protected void process(int e)
		{
		LogChar l=logChar.get(e);
		NetComp ctx=net.get(e);
		if(ctx.channel==null) // entity not finished to be created
			return;
		Channel chan=ctx.channel;

		world.delete(e);
		Integer st=manager.getEntity(l.uuid);
		if(st==null)
			{
			chan.writeAndFlush(ErrorComp.INVALID_STATE);
			chan.close();
			log.debug("failed to get State '{}' on log", l.uuid);
			return;
			}
		StateComp s=state.get(st);
		if(s==null)
			{
			chan.writeAndFlush(ErrorComp.INVALID_UUID);
			chan.close();
			return;
			}

		boolean ok=false;
		try
			{
			ok=database.loadPj(s.account.getId(), l.character, st);
			}
		catch (Exception ex)
			{
			log.error("failed to log char", ex);
			}
		if(!ok)
			{
			chan.writeAndFlush(ErrorComp.UNKNOWN_ERROR);
			chan.close();
			return;
			}

		s.state=States.IN_GAME;

		PositionComp p=position.get(st);
		MobInfoComp m=mobInfo.get(st);
		log.info("log char {} {}", l.uuid, m.name);
		Body b=body.get(st);

		// update calculated values
		update.process(st);
		CalculatedComp c=calculated.get(st);

		// spawn the new pj in the world
		gameWorld.spawn(s, st);
		PjInfo pjInfo=new PjInfo(m.name, p.x, p.y, m.hp, m.mp, b, c);
		chan.write(pjInfo);

		// get all surrounding entity and notify the new player with them
		IntBag bag=locManager.get(p.x, p.y, range, null);
		for(int i=0; i<bag.size(); i++)
			{
			int em=bag.get(i);
			if(em==st)
				continue;
			UUID uuid=manager.getUuid(em);
			p=position.get(em);
			m=mobInfo.get(em);
			VelocityComp v=velocity.get(em);
			c=calculated.get(em);
			if(c!=null)
				m=new MobInfoComp(m, c);
			chan.write(new Spawn(uuid, sprite.get(em), m, p, v));
			}

		// insert the pj into the world
		locManager.inserted(st);
		chan.flush();
		}
	}
