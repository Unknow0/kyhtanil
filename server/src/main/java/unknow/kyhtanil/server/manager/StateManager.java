package unknow.kyhtanil.server.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

import unknow.kyhtanil.server.component.StateComp;

/**
 * clean up disconnected client
 */
public class StateManager extends IteratingSystem {
	private static final Logger log = LoggerFactory.getLogger(StateManager.class);

	private ComponentMapper<StateComp> state;

	/**
	 * create new StateManager
	 */
	public StateManager() {
		super(Aspect.all(StateComp.class));
	}

	@Override
	protected void process(int entityId) {
		StateComp s = state.get(entityId);
		if (!s.channel.isOpen()) {
			log.info("cleaning {}", s.account);
			world.delete(entityId);
		}
	}
}
