package unknow.kyhtanil.server.system.net;

import org.apache.logging.log4j.*;

import unknow.kyhtanil.common.*;
import unknow.kyhtanil.server.component.*;
import unknow.kyhtanil.server.manager.*;

import com.artemis.*;
import com.artemis.systems.*;

public class MoveSystem extends IteratingSystem
	{
	private static final Logger log=LogManager.getFormatterLogger(MoveSystem.class);

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

		world.delete(entityId);
		Integer e=UUIDManager.self().getEntity(m.uuid);
		if(e==null)
			{
			ctx.channel.close();
			log.debug("failed to get State '%s' on move", m.uuid);
			return;
			}
		// TODO check validity
		PositionComp p=position.get(e);
		p.x=m.x;
		p.y=m.y;

		VelocityComp v=velocity.get(e);
		v.dirX=m.dirX;
		v.dirY=m.dirY;
		}
	}
