package unknow.kyhtanil.client.system.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

import unknow.kyhtanil.client.artemis.UUIDManager;
import unknow.kyhtanil.common.component.BooleanComp;
import unknow.kyhtanil.common.component.net.Despawn;

public class DespawnSystem extends IteratingSystem
	{
	private static final Logger log=LoggerFactory.getLogger(DespawnSystem.class);
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
		log.info("{}", d);
		Integer mob=manager.getEntity(d.uuid);
		if(mob!=null)
			{
			manager.remove(mob);
			world.delete(mob);
			}
		world.delete(entityId);
		}
	}
