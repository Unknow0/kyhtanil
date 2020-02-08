package unknow.kyhtanil.server.component;

import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.BaseSystem;

import unknow.kyhtanil.common.component.PositionComp;
import unknow.kyhtanil.common.component.SpriteComp;
import unknow.kyhtanil.common.component.StatAgg;
import unknow.kyhtanil.common.component.StatBase;
import unknow.kyhtanil.common.component.StatPoint;
import unknow.kyhtanil.common.component.StatShared;
import unknow.kyhtanil.common.component.VelocityComp;

public class Archetypes extends BaseSystem {
	public Archetype login;

	public Archetype mob;
	public Archetype pj;
	public Archetype proj;

	@SuppressWarnings("unchecked")
	@Override
	protected void initialize() {
		login = new ArchetypeBuilder().add(StateComp.class).build(world);

		ArchetypeBuilder builder = new ArchetypeBuilder();
		builder.add(PositionComp.class, VelocityComp.class, SpriteComp.class);
		builder.add(StatShared.class, StatBase.class, StatAgg.class, StatModAggregator.class);
		builder.add(DamageListComp.class, Dirty.class);
		mob = builder.build(world);

		builder.add(StateComp.class, StatPoint.class);
		pj = builder.build(world);

		builder = new ArchetypeBuilder();
		builder.add(PositionComp.class, VelocityComp.class, Projectile.class, SpriteComp.class);
		proj = builder.build(world);
	}

	@Override
	protected void processSystem() {
	}
}
