package unknow.kyhtanil.server.system;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.Component;
import com.artemis.ComponentManager;
import com.artemis.utils.Bag;

public class DebugSystem extends BaseEntitySystem
	{
	private ComponentManager manager;
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

		System.out.println("added "+entityId);
		for(int i=0; i<fillBag.size(); i++)
			System.out.println("	"+fillBag.get(i).getClass());
		}

	@Override
	protected void removed(int entityId)
		{
		System.out.println("removed "+entityId);
		}
	}
