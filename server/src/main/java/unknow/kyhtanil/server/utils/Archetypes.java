package unknow.kyhtanil.server.utils;

import unknow.kyhtanil.common.component.*;
import unknow.kyhtanil.server.component.*;

import com.artemis.*;

public class Archetypes
	{
	public static Archetype mob;
	public static Archetype pj;

	@SuppressWarnings("unchecked")
	public static void init(World world)
		{
		ArchetypeBuilder builder=new ArchetypeBuilder();
		builder.add(PositionComp.class, VelocityComp.class);
		builder.add(MobInfoComp.class, Body.class);
		builder.add(DamageListComp.class, CalculatedComp.class);
		mob=builder.build(world);

		builder.add(StateComp.class);
		pj=builder.build(world);
		}
	}
