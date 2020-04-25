package unknow.kyhtanil.server.component;

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
	public Archetype login;

	public Archetype mob;
	public Archetype pj;
	public Archetype proj;
	public Archetype spawner;

	@SuppressWarnings("unchecked")
	@Override
	protected void initialize() {
		login = new ArchetypeBuilder().add(StateComp.class).build(world);

		ArchetypeBuilder builder = new ArchetypeBuilder();
		builder.add(Position.class, Velocity.class, Sprite.class);
		builder.add(StatShared.class, StatBase.class, StatAgg.class, StatModAggregator.class);
		builder.add(DamageListComp.class, Dirty.class);
		mob = builder.build(world);

		builder.add(StateComp.class, StatPoint.class);
		pj = builder.build(world);

		builder = new ArchetypeBuilder();
		builder.add(Position.class, Velocity.class, Projectile.class, Sprite.class);
		proj = builder.build(world);

		spawner = new ArchetypeBuilder().add(SpawnerComp.class).build(world);
	}

	@Override
	protected void processSystem() {
	}
}
