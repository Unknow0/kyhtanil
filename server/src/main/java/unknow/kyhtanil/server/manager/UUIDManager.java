package unknow.kyhtanil.server.manager;

import unknow.kyhtanil.common.pojo.*;
import unknow.kyhtanil.common.util.*;
import unknow.kyhtanil.server.*;
import unknow.kyhtanil.server.component.*;
import unknow.kyhtanil.server.component.StateComp.*;

import com.artemis.*;

public class UUIDManager extends BaseUUIDManager
	{
	private ComponentMapper<StateComp> state;

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

		StateComp s=state.getSafe(entityId);

		if(s==null||s.state==States.IN_GAME)
			{
			GameServer.world().despawn(null, entityId);
			remove(entityId);
			}
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
