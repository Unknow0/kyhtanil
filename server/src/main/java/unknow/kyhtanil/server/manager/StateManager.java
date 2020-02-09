package unknow.kyhtanil.server.manager;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.esotericsoftware.kryo.util.IntMap;

import unknow.kyhtanil.common.pojo.UUID;
import unknow.kyhtanil.server.component.StateComp;

/**
 * manage the life cycle of State
 */
public class StateManager extends IteratingSystem {
	private static final Logger log = LoggerFactory.getLogger(StateManager.class);
	private final IntMap<UUID> uuidByLogin = new IntMap<>();
	private final Map<UUID, Integer> uuidToEntity = new HashMap<>();

	private UUIDManager uuidManager;

	private ComponentMapper<StateComp> state;

	public StateManager() {
		super(Aspect.all(StateComp.class));
	}

	@Override
	protected final void initialize() {
	}

	@Override
	public final void removed(int entityId) {
	}

	@Override
	public final void inserted(int e) {
	}

	public UUID log(int entity, int accountId) {
		if (uuidByLogin.containsKey(accountId))
			return null;
		UUID uuid = uuidManager.assignUuid(entity);

		uuidByLogin.put(accountId, uuid);
		uuidToEntity.put(uuid, entity);

		return uuid;
	}

	@Override
	protected void process(int entityId) {
		StateComp s = state.get(entityId);
		if (!s.channel.isOpen()) {
			log.info("cleaning {}", s.account);
			UUID uuid = uuidByLogin.remove(s.account);
			uuidToEntity.remove(uuid);
			world.delete(entityId);
		}
	}
}
