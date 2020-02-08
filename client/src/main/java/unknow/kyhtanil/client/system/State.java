package unknow.kyhtanil.client.system;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.annotations.SkipWire;

import unknow.kyhtanil.client.component.Archetypes;
import unknow.kyhtanil.common.component.SpriteComp;
import unknow.kyhtanil.common.component.StatBase;
import unknow.kyhtanil.common.component.StatPoint;
import unknow.kyhtanil.common.component.StatShared;
import unknow.kyhtanil.common.pojo.CharDesc;
import unknow.kyhtanil.common.pojo.UUID;

public class State extends BaseSystem {
	@SkipWire
	public static final State state = new State();

	private Archetypes arch;

	private ComponentMapper<StatShared> shared;
	private ComponentMapper<StatBase> base;
	private ComponentMapper<StatPoint> points;

	public CharDesc[] chars;
	public UUID uuid;
	public int entity = -1;

	private State() {
	}

	public void create() {
		if (entity >= 0)
			world.delete(entity);
		entity = world.create(arch.self);
		world.edit(entity).remove(SpriteComp.class);
		StatBase statBase = base.get(entity);
		statBase.level = 1;
		statBase.strength = 1;
		statBase.constitution = 1;
		statBase.intelligence = 1;
		statBase.concentration = 1;
		statBase.dexterity = 1;
		points.get(entity).base = 10;
	}

	@Override
	protected void initialize() {
		setEnabled(false);
	}

	@Override
	protected void processSystem() {
	}

	public StatShared shared() {
		return shared.get(entity);
	}

	public StatBase base() {
		return base.get(entity);
	}

	public StatPoint points() {
		return points.get(entity);
	}
}
