package unknow.kyhtanil.server.component;

import java.util.function.IntConsumer;

import com.artemis.PooledComponent;

import unknow.kyhtanil.common.pojo.UUID;

/**
 * a projectile
 * 
 * @author unknow
 */
public class Projectile extends PooledComponent {
	/** source of the projectile */
	public UUID source;
	/** the effect to apply on hit */
	public IntConsumer onHit;
	/** the remaining time */
	public float ttl;

	@Override
	protected void reset() {
		source = null;
		onHit = null;
	}
}
