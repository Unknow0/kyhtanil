package unknow.kyhtanil.server.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.Component;
import com.artemis.ComponentManager;
import com.artemis.utils.Bag;

import unknow.kyhtanil.common.util.BaseUUIDManager;

/**
 * log creation/deletion of entities
 * 
 * @author unknow
 */
public class DebugSystem extends BaseEntitySystem {
	private static final Logger log = LoggerFactory.getLogger(DebugSystem.class);
	private ComponentManager manager;
	private BaseUUIDManager uuid;
	private Bag<Component> fillBag = new Bag<>();

	/**
	 * create new DebugSystem
	 */
	public DebugSystem() {
		super(Aspect.all());
	}

	@Override
	protected void processSystem() {
	}

	@Override
	protected void inserted(int entityId) {
		fillBag.clear();
		manager.getComponentsFor(entityId, fillBag);

		log.trace("added {} {}", entityId, uuid.getUuid(entityId));
		for (int i = 0; i < fillBag.size(); i++)
			log.trace("	{}", fillBag.get(i));
	}

	@Override
	protected void removed(int entityId) {
		log.trace("removed {}", entityId);
	}
}
