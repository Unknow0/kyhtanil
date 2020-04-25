package unknow.kyhtanil.client.component;

import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.BaseSystem;

import unknow.kyhtanil.common.component.Position;
import unknow.kyhtanil.common.component.Sprite;
import unknow.kyhtanil.common.component.StatAgg;
import unknow.kyhtanil.common.component.StatBase;
import unknow.kyhtanil.common.component.StatPoint;
import unknow.kyhtanil.common.component.StatShared;
import unknow.kyhtanil.common.component.Velocity;

public class Archetypes extends BaseSystem {
	public Archetype all;
	public Archetype self;

	@SuppressWarnings("unchecked")
	@Override
	protected void initialize() {
		ArchetypeBuilder builder = new ArchetypeBuilder();
		builder.add(StatShared.class, Position.class, Velocity.class, Sprite.class);
		all = builder.build(world);

		builder.add(StatBase.class, StatAgg.class, StatPoint.class);
		self = builder.build(world);
		setEnabled(false);
	}

	@Override
	protected void processSystem() {
	}
}