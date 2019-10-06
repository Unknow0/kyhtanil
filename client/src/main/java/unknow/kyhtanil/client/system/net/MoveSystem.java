package unknow.kyhtanil.client.system.net;

import org.slf4j.*;

import unknow.kyhtanil.client.State;
import unknow.kyhtanil.client.artemis.*;
import unknow.kyhtanil.common.component.*;
import unknow.kyhtanil.common.component.net.*;

import com.artemis.*;
import com.artemis.systems.*;

public class MoveSystem extends IteratingSystem
	{
	private static final Logger log=LoggerFactory.getLogger(MoveSystem.class);
	private ComponentMapper<Move> move;
	private ComponentMapper<BooleanComp> done;

	private ComponentMapper<PositionComp> position;
	private UUIDManager manager;

	public MoveSystem()
		{
		super(Aspect.all(Move.class, BooleanComp.class));
		}

	protected void process(int entityId)
		{
		Move m=move.get(entityId);
		BooleanComp b=done.get(entityId);
		if(!b.value) // entity not finished to be created
			return;
		world.delete(entityId);
		log.info("move {}", m);
		int e=manager.getEntity(m.uuid);
		log.info("=> {} / {}", e, State.entity);
		PositionComp p=position.get(e);
		p.x=m.x;
		p.y=m.y;
		}
	}
