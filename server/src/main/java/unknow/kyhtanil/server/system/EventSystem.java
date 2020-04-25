package unknow.kyhtanil.server.system;

import java.util.HashSet;
import java.util.Set;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.utils.IntBag;

import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;

public class EventSystem extends BaseEntitySystem {
	private final IntObjectMap<Set<EntityListener>> listeners = new IntObjectHashMap<>();

	public EventSystem() {
		super(Aspect.all());
	}

	@Override
	protected void processSystem() {
	}

	@Override
	protected boolean checkProcessing() {
		return false;
	}

	@Override
	public void inserted(IntBag entities) {
		int[] ids = entities.getData();
		for (int i = 0, s = entities.size(); s > i; i++) {
			Set<EntityListener> set = listeners.get(ids[i]);
			if (set == null)
				continue;
			for (EntityListener l : set)
				l.inserted(ids[i]);
		}
	}

	public void register(int entityId, EntityListener listener) {
		Set<EntityListener> set = listeners.get(entityId);
		if (set == null)
			listeners.put(entityId, set = new HashSet<>());
		set.add(listener);
	}

	public void unregister(int entityId, EntityListener listener) {
		Set<EntityListener> set = listeners.get(entityId);
		if (set != null)
			set.remove(listener);
	}

	@Override
	public void removed(IntBag entities) {
		int[] ids = entities.getData();
		for (int i = 0, s = entities.size(); s > i; i++) {
			Set<EntityListener> set = listeners.remove(ids[i]);
			if (set != null) {
				for (EntityListener l : set)
					l.removed(ids[i]);
			}
		}
	}

	public static interface EntityListener {
		public void inserted(int entityId);

		public void removed(int entityId);
	}
}
