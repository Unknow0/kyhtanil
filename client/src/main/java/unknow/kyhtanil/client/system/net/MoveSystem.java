package unknow.kyhtanil.client.system.net;

import unknow.kyhtanil.client.artemis.*;
import unknow.kyhtanil.common.*;
import unknow.kyhtanil.common.component.*;

import com.artemis.*;
import com.artemis.systems.*;

public class MoveSystem extends IteratingSystem
	{
	private ComponentMapper<Move> move;
	private ComponentMapper<BooleanComp> done;
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

		Builder.update(manager.getEntity(m.uuid), m);
		}
	}
