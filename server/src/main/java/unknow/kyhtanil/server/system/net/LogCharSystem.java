package unknow.kyhtanil.server.system.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.artemis.utils.IntBag;

import io.netty.channel.Channel;
import unknow.kyhtanil.common.component.Body;
import unknow.kyhtanil.common.component.ErrorComp;
import unknow.kyhtanil.common.component.PositionComp;
import unknow.kyhtanil.common.component.SpriteComp;
import unknow.kyhtanil.common.component.StatShared;
import unknow.kyhtanil.common.component.VelocityComp;
import unknow.kyhtanil.common.component.account.LogChar;
import unknow.kyhtanil.common.component.account.PjInfo;
import unknow.kyhtanil.common.component.net.NetComp;
import unknow.kyhtanil.common.component.net.Spawn;
import unknow.kyhtanil.common.pojo.UUID;
import unknow.kyhtanil.server.Database;
import unknow.kyhtanil.server.component.StateComp;
import unknow.kyhtanil.server.component.StateComp.States;
import unknow.kyhtanil.server.manager.LocalizedManager;
import unknow.kyhtanil.server.manager.UUIDManager;
import unknow.kyhtanil.server.system.UpdateStatSystem;
import unknow.kyhtanil.server.utils.Archetypes;

/**
 * Manage the LogChar request
 * <br>Assign the char entity to the State uuid.
 */
public class LogCharSystem extends IteratingSystem
	{
	private static final Logger log=LoggerFactory.getLogger(LogCharSystem.class);

	private Clients clients;
	private Database database;
	private LocalizedManager locManager;
	private UUIDManager manager;

	private ComponentMapper<LogChar> logChar;
	private ComponentMapper<NetComp> net;
	private ComponentMapper<StateComp> state;
	private ComponentMapper<PositionComp> position;
	private ComponentMapper<SpriteComp> sprite;
	private ComponentMapper<VelocityComp> velocity;
	private ComponentMapper<StatShared> mobInfo;
	private ComponentMapper<Body> body;

	private UpdateStatSystem update;

	private static final float range=50f;

	public LogCharSystem()
		{
		super(Aspect.all(LogChar.class, NetComp.class));
		}

	@Override
	protected void process(int e)
		{
		LogChar l=logChar.get(e);
		NetComp ctx=net.get(e);
		Channel chan=ctx.channel;

		world.delete(e);
		Integer login=manager.getEntity(l.uuid);
		if(login==null)
			{
			chan.writeAndFlush(ErrorComp.INVALID_STATE);
			chan.close();
			log.debug("failed to get State '{}' on log", l.uuid);
			return;
			}
		StateComp s=state.get(login);
		if(s==null)
			{
			chan.writeAndFlush(ErrorComp.INVALID_UUID);
			chan.close();
			return;
			}

		int st=world.create(Archetypes.pj);
		UUID uuid=manager.getUuid(login);
		manager.setUuid(login, null);
		manager.setUuid(st, uuid);
		world.delete(login);
		state.get(st).set(s);
		s=state.get(st);

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
		StatShared m=mobInfo.get(st);
		log.info("log char {} {}", l.uuid, m.name);
		Body b=body.get(st);

		// update calculated values
		update.process(st);

		// spawn the new pj in the world
		clients.spawn(s, st);
		PjInfo pjInfo=new PjInfo(m.name, p.x, p.y, b, m);
		chan.write(pjInfo);

		// get all surrounding entity and notify the new player with them
		IntBag bag=locManager.get(p.x, p.y, range, null);
		for(int i=0; i<bag.size(); i++)
			{
			int em=bag.get(i);
			if(em==st)
				continue;
			uuid=manager.getUuid(em);
			p=position.get(em);
			m=mobInfo.get(em);
			VelocityComp v=velocity.get(em);
			chan.write(new Spawn(uuid, sprite.get(em), m, p, v));
			}
		chan.flush();
		}
	}
