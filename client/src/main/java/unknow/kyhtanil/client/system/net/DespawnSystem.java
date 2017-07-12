package unknow.kyhtanil.client.system.net;

import unknow.kyhtanil.client.artemis.*;
import unknow.kyhtanil.common.component.*;
import unknow.kyhtanil.common.component.net.*;

import com.artemis.*;
import com.artemis.systems.*;

public class DespawnSystem extends IteratingSystem
	{
	private ComponentMapper<Despawn> despawn;
	private ComponentMapper<BooleanComp> done;
	private UUIDManager manager;

	public DespawnSystem()
		{
		super(Aspect.all(Despawn.class, BooleanComp.class));
		}

	protected void process(int entityId)
		{
		Despawn d=despawn.get(entityId);
		BooleanComp b=done.get(entityId);
		if(!b.value) // entity not finished to be created
			return;

		world.delete(entityId);
		int mob=manager.getEntity(d.uuid);
		world.delete(mob);
		}
	}
