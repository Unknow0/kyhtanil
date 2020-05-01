package unknow.kyhtanil.server.manager;

import com.artemis.Aspect;
import com.artemis.utils.IntBag;

import unknow.kyhtanil.common.component.Sprite;
import unknow.kyhtanil.common.pojo.UUID;
import unknow.kyhtanil.common.util.BaseUUIDManager;
import unknow.kyhtanil.server.utils.UUIDGen;

/**
 * server uuid manager
 * 
 * @author unknow
 */
public class UUIDManager extends BaseUUIDManager {
	private UUIDGen uuidGen;
	private IntBag deleted = new IntBag();

	/**
	 * create new UUIDManager
	 */
	public UUIDManager() {
		super(Aspect.all(Sprite.class));
		uuidGen = new UUIDGen();
	}

	@Override
	public final void removed(int entityId) {
		UUID uuid = getUuid(entityId);
		if (uuid == null)
			return;
		deleted.add(entityId);
	}

	@Override
	public final void inserted(int e) {
		if (getUuid(e) == null) {// only mob got here
			assignUuid(e);
		}
	}

	/**
	 * assign an uuid to the entity
	 * 
	 * @param e the entity to assign an uuid
	 * @return the generated uuid
	 */
	public final UUID assignUuid(int e) {
		UUID uuid = getUuid(e);
		if (uuid != null)
			return uuid;
		uuid = uuidGen.next();
		while (uuidToEntity.containsKey(uuid))
			uuid = uuidGen.next();
		setUuid(e, uuid);
		return uuid;
	}

	@Override
	protected void processSystem() {
		for (int i = 0; i < deleted.size(); i++)
			remove(deleted.get(i));
		deleted.clear();
	}
}
