package unknow.kyhtanil.server.manager;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;

import unknow.kyhtanil.common.component.Sprite;
import unknow.kyhtanil.common.pojo.UUID;
import unknow.kyhtanil.common.util.BaseUUIDManager;
import unknow.kyhtanil.server.component.StateComp;
import unknow.kyhtanil.server.component.StateComp.States;
import unknow.kyhtanil.server.system.net.Clients;
import unknow.kyhtanil.server.utils.UUIDGen;

public class UUIDManager extends BaseUUIDManager {
	private ComponentMapper<StateComp> state;
	private UUIDGen uuidGen;
	private Clients clients;

	public UUIDManager() {
		super(Aspect.all(Sprite.class));
		uuidGen = new UUIDGen();
	}

	@Override
	public final void removed(int entityId) {
		UUID uuid = getUuid(entityId);
		if (uuid == null)
			return;

		StateComp s = state.get(entityId);

		clients.despawn(null, entityId);
		if (s == null || s.state == States.IN_GAME) {
			remove(entityId);
		}
	}

	@Override
	public final void inserted(int e) {
		if (getUuid(e) == null) // only mob got here
		{
			assignUuid(e);
		}
	}

	public final UUID assignUuid(int e) {
		UUID uuid = uuidGen.next();
		while (uuidToEntity.containsKey(uuid))
			uuid = uuidGen.next();
		setUuid(e, uuid);
		return uuid;
	}

	@Override
	protected void processSystem() {
	}
}
