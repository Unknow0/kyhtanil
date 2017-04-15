package unknow.kyhtanil.server.system;

import com.artemis.*;
import com.artemis.utils.*;
import com.esotericsoftware.kryo.util.*;

public class EventSystem extends BaseEntitySystem
	{
	private IntMap<EntityListener> listeners=new IntMap<>();

	public EventSystem(Aspect.Builder aspect)
		{
		super(aspect);
		}

	@Override
	protected void processSystem()
		{
		}

	@Override
	protected boolean checkProcessing()
		{
		return false;
		}

	@Override
	public void inserted(IntBag entities)
		{
		int[] ids=entities.getData();
		for(int i=0, s=entities.size(); s>i; i++)
			{
			EntityListener l=listeners.get(ids[i]);
			if(l!=null)
				l.inserted(ids[i]);
			}
		}

	public void register(int entityId, EntityListener listener)
		{
		listeners.put(entityId, listener);
		}

	@Override
	public void removed(IntBag entities)
		{
		int[] ids=entities.getData();
		for(int i=0, s=entities.size(); s>i; i++)
			{
			EntityListener l=listeners.remove(ids[i]);
			if(l!=null)
				l.removed(ids[i]);
			}
		}

	public static interface EntityListener
		{
		public void inserted(int entityId);

		public void removed(int entityId);
		}
	}
