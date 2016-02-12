package unknow.kyhtanil.server.system;

import unknow.kyhtanil.server.component.*;

import com.artemis.*;
import com.artemis.systems.*;

public class SpawnSystem extends IteratingSystem
	{
	private ComponentMapper<SpawnerComp> spawner;
	private ComponentMapper<PositionComp> position;
	private ComponentMapper<VelocityComp> velocity;
	private ComponentMapper<MobInfoComp> mobInfo;

	private Archetype mob;

	public SpawnSystem()
		{
		super(Aspect.all(SpawnerComp.class));
		}

	@SuppressWarnings("unchecked")
	@Override
	protected void initialize()
		{
		super.initialize();
		spawner=ComponentMapper.getFor(SpawnerComp.class, world);
		position=ComponentMapper.getFor(PositionComp.class, world);
		velocity=ComponentMapper.getFor(VelocityComp.class, world);
		mobInfo=ComponentMapper.getFor(MobInfoComp.class, world);
		mob=new ArchetypeBuilder().add(PositionComp.class, VelocityComp.class, MobInfoComp.class, DamageListComp.class).build(world);
		}

	@Override
	protected void process(int e)
		{
		SpawnerComp s=spawner.get(e);
		if(s.current_count>s.max_count)
			return;
		s.current+=world.delta*s.creation_speed;

		if(s.current>1)
			{
			s.current=0;
			s.current_count++;
			Entity m=world.createEntity(mob);

			PositionComp p=position.get(m);
			p.x=(float)(s.x+Math.random()*s.range*2-s.range);
			p.y=(float)(s.y+Math.random()*s.range*2-s.range);

			VelocityComp v=velocity.get(m);
			v.dirX=v.dirY=0;
			v.speed=1f;

			MobInfoComp mi=mobInfo.get(m);
			mi.hp=20;
			mi.name="mob";
			mi.level=1;
			}
		}
	}
