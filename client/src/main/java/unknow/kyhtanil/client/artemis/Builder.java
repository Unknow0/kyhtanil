package unknow.kyhtanil.client.artemis;

import unknow.kyhtanil.client.component.*;
import unknow.kyhtanil.common.*;

import com.artemis.*;
import com.artemis.utils.*;
import com.badlogic.gdx.graphics.g2d.*;

public class Builder
	{
	private static World world;

	private static ComponentMapper<PositionComp> positionMapper;
	private static ComponentMapper<SpriteComp> spriteMapper;
	private static ComponentMapper<VelocityComp> velocityMapper;
	private static ComponentMapper<TargetComp> target;

	private static EntitySubscription allTarget;

	private static Archetype mobArch;

	public Builder()
		{
		}

	@SuppressWarnings("unchecked")
	public static void init(World world)
		{
		Builder.world=world;
		Builder.positionMapper=ComponentMapper.getFor(PositionComp.class, world);
		Builder.spriteMapper=ComponentMapper.getFor(SpriteComp.class, world);
		Builder.target=ComponentMapper.getFor(TargetComp.class, world);
		Builder.velocityMapper=ComponentMapper.getFor(VelocityComp.class, world);

		Builder.mobArch=new ArchetypeBuilder().add(SpriteComp.class, PositionComp.class, VelocityComp.class).build(world);

		AspectSubscriptionManager sm=world.getAspectSubscriptionManager();
		allTarget=sm.get(Aspect.all(TargetComp.class));
		}

	public static PositionComp getPosition(int id)
		{
		return positionMapper.get(id);
		}

	public static SpriteComp getSprite(int id)
		{
		return spriteMapper.get(id);
		}

	public static VelocityComp getVelocity(int id)
		{
		return velocityMapper.get(id);
		}

	public static int buildMob(float x, float y, TextureRegion tex, float w, float h)
		{
		int e=world.create(mobArch);
		PositionComp p=getPosition(e);
		p.x=x;
		p.y=y;

		SpriteComp s=getSprite(e);
		s.tex=tex;
		s.w=w;
		s.h=h;

		return e;
		}

	public static void update(int mob, Move move)
		{
		PositionComp p=getPosition(mob);
		p.x=move.x;
		p.y=move.y;

		// TODO velocity
		}

	public static boolean isTarget(int e)
		{
		return target.has(e);
		}

	public static IntBag getTarget()
		{
		return allTarget.getEntities();
		}
	}
