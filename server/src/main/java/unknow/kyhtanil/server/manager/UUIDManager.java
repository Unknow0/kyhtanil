package unknow.kyhtanil.server.manager;

import unknow.json.*;
import unknow.kyhtanil.common.component.SpriteComp;
import unknow.kyhtanil.common.pojo.*;
import unknow.kyhtanil.common.util.*;
import unknow.kyhtanil.server.*;
import unknow.kyhtanil.server.component.*;
import unknow.kyhtanil.server.component.StateComp.States;
import unknow.kyhtanil.server.utils.*;

import com.artemis.*;

public class UUIDManager extends BaseUUIDManager
	{
	private GameWorld gameWorld;
	private ComponentMapper<StateComp> state;
	private UUIDGen uuidGen;

	public UUIDManager(GameWorld gameWorld) throws JsonException
		{
		super(Aspect.all(SpriteComp.class));
		this.gameWorld=gameWorld;
		uuidGen=new UUIDGen();
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

		StateComp s=state.get(entityId);

		if(s==null||s.state==States.IN_GAME)
			{
			gameWorld.despawn(null, entityId);
			remove(entityId);
			}
		}

	@Override
	public final void inserted(int e)
		{
		if(getUuid(e)==null) // only mob got here
			{
			assignUuid(e);
			gameWorld.spawn(null, e);
			}
		}

	public final UUID assignUuid(int e)
		{
		UUID uuid=uuidGen.next();
		while (uuidToEntity.containsKey(uuid))
			uuid=uuidGen.next();
		setUuid(e, uuid);
		return uuid;
		}

	protected void processSystem()
		{
		}
	}
