package unknow.kyhtanil.client.artemis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.Aspect;
import com.artemis.AspectSubscriptionManager;
import com.artemis.BaseComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.utils.IntBag;

import unknow.common.tools.JsonUtils;
import unknow.kyhtanil.client.component.TargetComp;
import unknow.kyhtanil.common.component.CalculatedComp;
import unknow.kyhtanil.common.component.MobInfoComp;
import unknow.kyhtanil.common.component.PositionComp;
import unknow.kyhtanil.common.component.SpriteComp;
import unknow.kyhtanil.common.component.VelocityComp;
import unknow.kyhtanil.common.component.net.Move;

public class Builder
	{
	private static final Logger log=LoggerFactory.getLogger(Builder.class);
	private static World world;

	private static BaseComponentMapper<PositionComp> positionMapper;
	private static BaseComponentMapper<SpriteComp> spriteMapper;
	private static BaseComponentMapper<VelocityComp> velocityMapper;
	private static BaseComponentMapper<TargetComp> target;
	private static BaseComponentMapper<CalculatedComp> calculated;

	private static EntitySubscription allTarget;

	public static Archetype mobArch;
	public static Archetype pjArch;

	public Builder()
		{
		}

	@SuppressWarnings("unchecked")
	public static void init(World world)
		{
		Builder.world=world;
		Builder.positionMapper=BaseComponentMapper.getFor(PositionComp.class, world);
		Builder.spriteMapper=BaseComponentMapper.getFor(SpriteComp.class, world);
		Builder.target=BaseComponentMapper.getFor(TargetComp.class, world);
		Builder.velocityMapper=BaseComponentMapper.getFor(VelocityComp.class, world);
		Builder.calculated=BaseComponentMapper.getFor(CalculatedComp.class, world);

		Builder.mobArch=new ArchetypeBuilder().add(SpriteComp.class, PositionComp.class, VelocityComp.class, MobInfoComp.class).build(world);
		Builder.pjArch=new ArchetypeBuilder(mobArch).add(CalculatedComp.class).build(world);

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

	public static int buildMob(float x, float y, SpriteComp sprite, CalculatedComp total, float direction)
		{
		int e=world.create(mobArch);
		PositionComp p=getPosition(e);
		p.x=x;
		p.y=y;

		getSprite(e).set(sprite);

		VelocityComp v=velocityMapper.get(e);
		v.direction=direction;

		log.debug("buildMod: {}", JsonUtils.toString(total));
		if(total!=null)
			{
			CalculatedComp calc=calculated.get(e);
			calc.set(total);
			v.speed=calc.moveSpeed;
			}

		return e;
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
