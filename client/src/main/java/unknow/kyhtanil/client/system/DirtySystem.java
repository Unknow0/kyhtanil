package unknow.kyhtanil.client.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

import unknow.kyhtanil.client.system.net.Connection;
import unknow.kyhtanil.common.component.Dirty;
import unknow.kyhtanil.common.component.Position;

/**
 * send &amp; clear dirty event
 * 
 * @author unknow
 */
public class DirtySystem extends IteratingSystem {
	private ComponentMapper<Dirty> dirty;

	private State state;
	private Connection co;

	/**
	 * create new DirtySystem
	 */
	public DirtySystem() {
		super(Aspect.all(Dirty.class, Position.class));
	}

	@Override
	protected void process(int e) {
		Dirty d = dirty.get(e);
		if (d.map.isEmpty())
			return;
		co.write(d.changed(state.uuid));
		d.reset();
	}
}
