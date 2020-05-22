package unknow.kyhtanil.client.system;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.annotations.SkipWire;

import unknow.kyhtanil.common.component.Dirty;
import unknow.kyhtanil.common.component.Inventory;
import unknow.kyhtanil.common.component.Setable;
import unknow.kyhtanil.common.component.StatBase;
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
	private ComponentMapper<Inventory> inventory;
	private ComponentMapper<Dirty> dirty;

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
	 * @return the inventory
	 */
	public Inventory inventory() {
		return inventory.get(entity);
	}

	/**
	 * @return the base stat of the current char
	 */
	public StatBase base() {
		return base.get(entity);
	}

	/**
	 * mark component as dirty
	 * 
	 * @param component
	 */
	public void dirty(Setable<?>... component) {
		Dirty d = dirty.get(entity);
		for (Setable<?> c : component)
			d.add(c);
	}
}
