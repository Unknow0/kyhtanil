/**
 * 
 */
package unknow.kyhtanil.server.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

import unknow.kyhtanil.server.component.TTL;

/**
 * @author unknow
 */
public class TtlSystem extends IteratingSystem {
	private ComponentMapper<TTL> ttl;

	/**
	 * create new TtlSystem
	 */
	public TtlSystem() {
		super(Aspect.all(TTL.class));
	}

	@Override
	protected void process(int entityId) {
		TTL e = ttl.get(entityId);
		e.ttl -= world.delta;
		if (e.ttl <= 0)
			world.delete(entityId);
	}
}
