package unknow.kyhtanil.common.util;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artemis.Aspect;
import com.artemis.Aspect.Builder;
import com.artemis.BaseEntitySystem;
import com.artemis.utils.Bag;

import unknow.kyhtanil.common.pojo.UUID;

/**
 * Manager of uuid, keep mapping between uuid and entities
 * 
 * @author unknow
 */
public class BaseUUIDManager extends BaseEntitySystem {
	private static final Logger log = LoggerFactory.getLogger(BaseUUIDManager.class);

	protected final Map<UUID, Integer> uuidToEntity;
	private final Bag<UUID> entityToUuid;

	/**
	 * create new BaseUUIDManager
	 */
	public BaseUUIDManager() {
		this(Aspect.all());
	}

	protected BaseUUIDManager(Builder aspect) {
		super(aspect);
		this.uuidToEntity = new HashMap<>();
		this.entityToUuid = new Bag<>();
	}

	/**
	 * @param uuid
	 * @return the entity or null if this uuid is unknown
	 */
	public final Integer getEntity(UUID uuid) {
		return uuidToEntity.get(uuid);
	}

	/**
	 * @param entityId
	 * @return the uuid of this entities
	 */
	public final UUID getUuid(int entityId) {
		return entityToUuid.safeGet(entityId);
	}

	/**
	 * remove the uuid of this entrie
	 * 
	 * @param e the entitie to remove
	 * @return the previous uuid if any
	 */
	public final UUID remove(int e) {
		UUID uuid = entityToUuid.safeGet(e);
		if (uuid != null) {
			log.debug("remove {} {}", e, uuid);
			uuidToEntity.remove(uuid);
			entityToUuid.set(e, null);
		}
		return uuid;
	}

	/**
	 * set this uuid to this entity. <br>
	 * Remove old uuid maping for this entity and uuid
	 * 
	 * @param entityId the entity
	 * @param newUuid  the new uuid
	 */
	public final void setUuid(int entityId, UUID newUuid) {
		log.info("setUuuid {} {}", entityId, newUuid);
		Integer oldEntity = uuidToEntity.get(newUuid);
		if (oldEntity != null)
			remove(oldEntity);

		UUID oldUuid = entityToUuid.safeGet(entityId);
		if (oldUuid != null)
			uuidToEntity.remove(oldUuid);

		uuidToEntity.put(newUuid, entityId);
		entityToUuid.set(entityId, newUuid);
	}

	@Override
	protected void processSystem() {
	}
}
