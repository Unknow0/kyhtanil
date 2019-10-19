package unknow.kyhtanil.server.system.net;

import java.io.*;

import io.netty.channel.*;

import org.slf4j.*;

import unknow.kyhtanil.common.component.*;
import unknow.kyhtanil.common.component.net.*;
import unknow.kyhtanil.common.maps.*;
import unknow.kyhtanil.server.*;
import unknow.kyhtanil.server.component.*;
import unknow.kyhtanil.server.manager.*;

import com.artemis.*;
import com.artemis.annotations.*;
import com.artemis.systems.*;

public class MoveSystem extends IteratingSystem
	{
	private static final Logger log=LoggerFactory.getLogger(MoveSystem.class);

	private UUIDManager manager;
	private ComponentMapper<Move> move;
	private ComponentMapper<NetComp> net;
	private ComponentMapper<PositionComp> position;
	private ComponentMapper<VelocityComp> velocity;
	private ComponentMapper<CalculatedComp> calculated;
	private ComponentMapper<StateComp> state;
	private ComponentMapper<Dirty> dirty;

	private LocalizedManager locManager;

	@Wire
	private MapLayout layout;

	public MoveSystem(GameWorld gameWorld)
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

		CalculatedComp c=calculated.get(e);
		PositionComp p=position.get(e);
		StateComp sender=state.get(e);

		try
			{
			if(layout.isWall((int)(m.x/4), (int)(m.y/4))||p.distance(m.x, m.y)>c.moveSpeed+.1) //TODO
				{
				sender=null;
				m=new Move(m.uuid, p.x, p.y, m.direction);
				}
			}
		catch (IOException e1)
			{
			// TODO Auto-generated catch block
			e1.printStackTrace();
			}
//		else
//			{
		p.x=m.x;
		p.y=m.y;
		locManager.changed(e);
		VelocityComp v=velocity.get(e);
		v.direction=m.direction;
//			}

		dirty.get(e).dirty=true;
		}
	}
