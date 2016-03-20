package unknow.kyhtanil.server.manager;

import unknow.kyhtanil.common.pojo.*;
import unknow.kyhtanil.common.util.*;
import unknow.kyhtanil.server.*;
import unknow.kyhtanil.server.component.*;

import com.artemis.*;

public class UUIDManager extends BaseUUIDManager
	{
	public UUIDManager()
		{
		super(Aspect.all(MobInfoComp.class));
		}

	@Override
	protected final void initialize()
		{
		}

	@Override
	public final void removed(int entityId)
		{
		UUID uuid=getUuid(entityId);
		if(uuid==null)
			return;

		GameServer.world().despawn(null, entityId);
		remove(entityId);
		}

	@Override
	public final void inserted(int e)
		{
		if(getUuid(e)==null) // only mob got here
			{
			assignUuid(e);
			GameServer.world().spawn(null, e);
			}
		}

	protected void processSystem()
		{
		}
	}
