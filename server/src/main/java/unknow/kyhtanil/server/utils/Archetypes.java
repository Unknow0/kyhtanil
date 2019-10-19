package unknow.kyhtanil.server.utils;

import unknow.kyhtanil.common.component.*;
import unknow.kyhtanil.server.component.*;

import com.artemis.*;

public class Archetypes
	{
	public static Archetype mob;
	public static Archetype pj;
	public static Archetype proj;

	@SuppressWarnings("unchecked")
	public static void init(World world)
		{
		ArchetypeBuilder builder=new ArchetypeBuilder();
		builder.add(PositionComp.class, VelocityComp.class, SpriteComp.class);
		builder.add(MobInfoComp.class, Body.class, AggregatedStat.class);
		builder.add(DamageListComp.class, CalculatedComp.class);
		builder.add(Dirty.class);
		mob=builder.build(world);

		builder.add(StateComp.class);
		pj=builder.build(world);

		builder=new ArchetypeBuilder();
		builder.add(PositionComp.class, VelocityComp.class, Projectile.class, SpriteComp.class);
		proj=builder.build(world);
		}
	}
