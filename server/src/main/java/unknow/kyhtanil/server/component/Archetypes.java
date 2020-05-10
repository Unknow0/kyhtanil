package unknow.kyhtanil.server.component;

import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.BaseSystem;

import unknow.kyhtanil.common.component.Dirty;
import unknow.kyhtanil.common.component.Position;
import unknow.kyhtanil.common.component.Sprite;
import unknow.kyhtanil.common.component.StatAgg;
import unknow.kyhtanil.common.component.StatBase;
import unknow.kyhtanil.common.component.StatShared;
import unknow.kyhtanil.common.component.Velocity;

/**
 * server archetypes
 * 
 * @author unknow
 */
public class Archetypes extends BaseSystem {
	/** archetype of logged account */
	public Archetype login;

	/** mob entities */
	public Archetype mob;
	/** all player char */
	public Archetype pj;
	/** a projectile */
	public Archetype proj;
	/** a mob spawner */
	public Archetype spawner;
	/** managed mob entities */
	public Archetype managerMob;

	/** instance of damage */
	public Archetype damage;

	@SuppressWarnings("unchecked")
	@Override
	protected void initialize() {
		login = new ArchetypeBuilder().add(StateComp.class).build(world);

		ArchetypeBuilder builder = new ArchetypeBuilder();
		builder.add(Position.class, Velocity.class, Sprite.class);
		builder.add(StatShared.class, StatBase.class, StatAgg.class, StatModAggregator.class);
		builder.add(Dirty.class);
		mob = builder.build(world);
		managerMob = new ArchetypeBuilder(mob).add(Spawned.class, Contribution.class).build(world);

		builder.add(StateComp.class);
		pj = builder.build(world);

		builder = new ArchetypeBuilder();
		builder.add(Dirty.class, Position.class, Velocity.class, Projectile.class, Sprite.class, TTL.class);
		proj = builder.build(world);

		spawner = new ArchetypeBuilder().add(Spawner.class).build(world);

		damage = new ArchetypeBuilder().add(Damage.class, TTL.class).build(world);
	}

	@Override
	protected void processSystem() {
	}
}
