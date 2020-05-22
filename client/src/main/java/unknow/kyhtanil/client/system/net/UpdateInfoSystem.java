package unknow.kyhtanil.client.system.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artemis.Aspect;
import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

import unknow.kyhtanil.client.system.State;
import unknow.kyhtanil.common.component.Setable;
import unknow.kyhtanil.common.component.net.UpdateInfo;
import unknow.kyhtanil.common.util.BaseUUIDManager;

/**
 * apply update info event
 * 
 * @author unknow
 */
public class UpdateInfoSystem extends IteratingSystem {
	private static final Logger log = LoggerFactory.getLogger(UpdateInfoSystem.class);
	private ComponentMapper<UpdateInfo> update;

	private BaseUUIDManager manager;
	private State state;

	/**
	 * create new UpdateInfoSystem
	 */
	public UpdateInfoSystem() {
		super(Aspect.all(UpdateInfo.class));
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void process(int entityId) {
		UpdateInfo u = update.get(entityId);
		world.delete(entityId);
		log.info("update {}", u);
		int e = manager.getEntity(u.uuid);
		log.info("=> {} / {}", e, state.entity);

		for (@SuppressWarnings("rawtypes") Setable s : u.c) {
			ComponentMapper<? extends Component> m = world.getMapper(s.component());
			s.setTo(m.get(e));
		}
	}
}
