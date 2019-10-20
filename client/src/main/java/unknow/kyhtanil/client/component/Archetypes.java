package unknow.kyhtanil.client.component;

import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.BaseSystem;

import unknow.kyhtanil.common.component.PositionComp;
import unknow.kyhtanil.common.component.SpriteComp;
import unknow.kyhtanil.common.component.StatPerso;
import unknow.kyhtanil.common.component.StatShared;
import unknow.kyhtanil.common.component.VelocityComp;

public class Archetypes extends BaseSystem
	{
	public Archetype all;
	public Archetype self;

	@SuppressWarnings("unchecked")
	@Override
	protected void initialize()
		{
		ArchetypeBuilder builder=new ArchetypeBuilder();
		builder.add(StatShared.class, PositionComp.class, VelocityComp.class, SpriteComp.class);
		all=builder.build(world);

		builder.add(StatPerso.class);
		self=builder.build(world);
		setEnabled(false);
		}

	@Override
	protected void processSystem()
		{
		}
	}