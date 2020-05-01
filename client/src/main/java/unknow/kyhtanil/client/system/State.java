package unknow.kyhtanil.client.system;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.annotations.SkipWire;

import unknow.kyhtanil.common.component.StatBase;
import unknow.kyhtanil.common.component.StatPoint;
import unknow.kyhtanil.common.component.StatShared;
import unknow.kyhtanil.common.pojo.CharDesc;
import unknow.kyhtanil.common.pojo.UUID;

/**
 * Used to share state between actor & entity system
 * 
 * @author unknow
 */
public class State extends BaseSystem {
	/** the global state */
	@SkipWire
	public static final State state = new State();

	private ComponentMapper<StatShared> shared;
	private ComponentMapper<StatBase> base;
	private ComponentMapper<StatPoint> points;

	/** the list of characters */
	public CharDesc[] chars;
	/** the uuid of this connection */
	public UUID uuid;
	/** the entity of the logged char */
	public int entity = -1;

	private State() {
	}

	@Override
	protected void initialize() {
		setEnabled(false);
	}

	@Override
	protected void processSystem() {
	}

	/**
	 * @return the shared stat of the current char
	 */
	public StatShared shared() {
		return shared.get(entity);
	}

	/**
	 * @return the base stat of the current char
	 */
	public StatBase base() {
		return base.get(entity);
	}

	/**
	 * @return the point stat of the current char
	 */
	public StatPoint points() {
		return points.get(entity);
	}
}
