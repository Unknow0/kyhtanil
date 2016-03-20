package unknow.kyhtanil.server.manager;

import unknow.common.data.*;
import unknow.kyhtanil.common.pojo.*;
import unknow.kyhtanil.common.util.*;
import unknow.kyhtanil.server.component.*;

import com.artemis.*;

/**
 * manage the life cycle of State
 */
public class StateManager extends BaseUUIDManager
	{
	private final BTree<String,UUID> uuidByLogin=new BTree<String,UUID>(10);
	private final BTree<UUID,Integer> uuidToEntity=new BTree<UUID,Integer>(10);

	private UUIDManager uuidManager;

	public StateManager()
		{
		super(Aspect.all(StateComp.class));
		}

	@Override
	protected final void initialize()
		{
		}

	@Override
	public final void removed(int entityId)
		{
		}

	@Override
	public final void inserted(int e)
		{
		}

	protected void processSystem()
		{
		}

	public UUID log(int entity, String login)
		{
		if(uuidByLogin.containsKey(login))
			return null;
		UUID uuid=uuidManager.assignUuid(entity);

		uuidByLogin.put(login, uuid);
		uuidToEntity.put(uuid, entity);

		return uuid;
		}
	}
