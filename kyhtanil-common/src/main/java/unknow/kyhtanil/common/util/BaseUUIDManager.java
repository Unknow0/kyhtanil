package unknow.kyhtanil.common.util;

import java.util.*;

import unknow.kyhtanil.common.UUID;

import com.artemis.Aspect.Builder;
import com.artemis.*;
import com.artemis.utils.*;

public class BaseUUIDManager extends BaseEntitySystem
	{
	private final Map<UUID,Integer> uuidToEntity;
	private final Bag<UUID> entityToUuid;

	protected BaseUUIDManager(Builder aspect)
		{
		super(aspect);
		this.uuidToEntity=new HashMap<UUID,Integer>();
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

	public final UUID assignUuid(int e)
		{
		UUID uuid=UUIDGen.next();
		while (uuidToEntity.containsKey(uuid))
			uuid=UUIDGen.next();
		setUuid(e, uuid);
		return uuid;
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

	public final void setUuid(int entityId, UUID newUuid)
		{
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
