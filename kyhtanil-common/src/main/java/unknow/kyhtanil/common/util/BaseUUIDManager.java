package unknow.kyhtanil.common.util;

import org.slf4j.*;

import unknow.common.data.*;
import unknow.kyhtanil.common.pojo.*;

import com.artemis.Aspect.Builder;
import com.artemis.*;
import com.artemis.utils.*;

public class BaseUUIDManager extends BaseEntitySystem
	{
	private static final Logger log=LoggerFactory.getLogger(BaseUUIDManager.class);
	protected final BTree<UUID,Integer> uuidToEntity;
	private final Bag<UUID> entityToUuid;

	protected BaseUUIDManager(Builder aspect)
		{
		super(aspect);
		this.uuidToEntity=new BTree<UUID,Integer>();
		this.entityToUuid=new Bag<UUID>();
		}

	public final Integer getEntity(UUID uuid)
		{
		return uuidToEntity.get(uuid);
		}

	public final UUID getUuid(int entityId)
		{
		return entityToUuid.safeGet(entityId);
		}

	public final UUID remove(int e)
		{
		UUID uuid=entityToUuid.safeGet(e);
		if(uuid!=null)
			{
			uuidToEntity.remove(uuid);
			entityToUuid.set(e, null);
			}
		return uuid;
		}

	/**
	 * set this uuid to this entity.
	 * <br>Remove old uuid maping for this entity and uuid
	 */
	public final void setUuid(int entityId, UUID newUuid)
		{
		log.info("setUuuid {} {}", entityId, newUuid);
		Integer oldEntity=uuidToEntity.get(newUuid);
		if(oldEntity!=null)
			remove(oldEntity);

		UUID oldUuid=entityToUuid.safeGet(entityId);
		if(oldUuid!=null)
			uuidToEntity.remove(oldUuid);

		uuidToEntity.put(newUuid, entityId);
		entityToUuid.set(entityId, newUuid);
		}

	protected void processSystem()
		{
		}
	}
