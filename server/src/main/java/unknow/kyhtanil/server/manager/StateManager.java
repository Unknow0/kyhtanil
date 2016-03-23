package unknow.kyhtanil.server.manager;

import org.slf4j.*;

import unknow.common.data.*;
import unknow.kyhtanil.common.pojo.*;
import unknow.kyhtanil.server.component.*;

import com.artemis.*;
import com.artemis.systems.*;

/**
 * manage the life cycle of State
 */
public class StateManager extends IteratingSystem
	{
	private static final Logger log=LoggerFactory.getLogger(StateManager.class);
	private final BTree<String,UUID> uuidByLogin=new BTree<String,UUID>(10);
	private final BTree<UUID,Integer> uuidToEntity=new BTree<UUID,Integer>(10);

	private UUIDManager uuidManager;

	private ComponentMapper<StateComp> state;

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

	public UUID log(int entity, String login)
		{
		if(uuidByLogin.containsKey(login))
			return null;
		UUID uuid=uuidManager.assignUuid(entity);

		uuidByLogin.put(login, uuid);
		uuidToEntity.put(uuid, entity);

		return uuid;
		}

	@Override
	protected void process(int entityId)
		{
		StateComp s=state.get(entityId);
		if(!s.channel.isOpen())
			{
			log.info("cleaning {}", s.account.getLogin());
			UUID uuid=uuidByLogin.remove(s.account.getLogin());
			uuidToEntity.remove(uuid);
			world.delete(entityId);
			}
		}
	}
