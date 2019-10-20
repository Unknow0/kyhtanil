package unknow.kyhtanil.server.system;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.Component;
import com.artemis.ComponentManager;
import com.artemis.managers.UuidEntityManager;
import com.artemis.utils.Bag;

import unknow.kyhtanil.common.util.BaseUUIDManager;
import unknow.kyhtanil.server.manager.UUIDManager;

public class DebugSystem extends BaseEntitySystem
	{
	private ComponentManager manager;
	private BaseUUIDManager uuid;
	private Bag<Component> fillBag=new Bag<>();

	public DebugSystem()
		{
		super(Aspect.all());
		}

	@Override
	protected void processSystem()
		{
		}

	@Override
	protected void inserted(int entityId)
		{
		fillBag.clear();
		manager.getComponentsFor(entityId, fillBag);

		System.out.println("added "+entityId+" "+uuid.getUuid(entityId));
		for(int i=0; i<fillBag.size(); i++)
			System.out.println("	"+fillBag.get(i).getClass());
		}

	@Override
	protected void removed(int entityId)
		{
		System.out.println("removed "+entityId);
		}
	}
