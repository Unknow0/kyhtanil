package unknow.kyhtanil.server.system.net;

import io.netty.channel.*;

import org.slf4j.*;

import unknow.kyhtanil.common.*;
import unknow.kyhtanil.common.component.*;
import unknow.kyhtanil.server.manager.*;

import com.artemis.*;
import com.artemis.systems.*;

public class MoveSystem extends IteratingSystem
	{
	private static final Logger log=LoggerFactory.getLogger(MoveSystem.class);

	private UUIDManager manager;
	private ComponentMapper<Move> move;
	private ComponentMapper<NetComp> net;
	private ComponentMapper<PositionComp> position;
	private ComponentMapper<VelocityComp> velocity;

	public MoveSystem()
		{
		super(Aspect.all(Move.class, NetComp.class));
		}

	protected void process(int entityId)
		{
		Move m=move.get(entityId);
		NetComp ctx=net.get(entityId);
		if(ctx.channel==null) // entity not finished to be created
			return;
		Channel chan=ctx.channel;

		world.delete(entityId);
		Integer e=manager.getEntity(m.uuid);
		if(e==null)
			{
			log.debug("failed to get State '{}' on move", m.uuid);
			chan.writeAndFlush(ErrorComp.INVALID_UUID);
			chan.close();
			return;
			}

		// TODO check validity
		PositionComp p=position.get(e);
		p.x=m.x;
		p.y=m.y;

		VelocityComp v=velocity.get(e);
		v.direction=m.direction;
		}
	}
