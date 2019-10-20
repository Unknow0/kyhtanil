package unknow.kyhtanil.server.system;

import unknow.kyhtanil.common.component.*;
import unknow.kyhtanil.server.component.*;
import unknow.kyhtanil.server.utils.*;

import com.artemis.*;
import com.artemis.systems.*;

public class SpawnSystem extends IteratingSystem
	{
	private ComponentMapper<SpawnerComp> spawner;
	private ComponentMapper<PositionComp> position;
	private ComponentMapper<VelocityComp> velocity;
	private ComponentMapper<StatShared> mobInfo;
	private ComponentMapper<SpriteComp> sprite;
	private ComponentMapper<AggregatedStat> stats;

	private UpdateStatSystem update;
	private EventSystem event;

	public SpawnSystem()
		{
		super(Aspect.all(SpawnerComp.class));
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
			int m=world.create(Archetypes.mob);

			PositionComp p=position.get(m);
			p.x=(float)(s.x+Math.random()*s.range*2-s.range);
			p.y=(float)(s.y+Math.random()*s.range*2-s.range);

			VelocityComp v=velocity.get(m);
			v.direction=0;
			v.speed=0f;

			StatShared mi=mobInfo.get(m);
			mi.hp=10;
			mi.name="mob"; // TODO
			
			AggregatedStat st=stats.get(m);
			// TODO load mob stats
			

			SpriteComp sp=sprite.get(m);
			sp.h=sp.w=3;
			sp.tex="data/tex/mob.png";

			update.process(m);
			event.register(m, new Listener(spawner, e));
			}
		}

	private static class Listener implements EventSystem.EntityListener
		{
		private ComponentMapper<SpawnerComp> spawner;
		private int spawnerId;

		public Listener(ComponentMapper<SpawnerComp> spawner, int spawnerId)
			{
			this.spawner=spawner;
			this.spawnerId=spawnerId;
			}

		@Override
		public void inserted(int entityId)
			{
			}

		@Override
		public void removed(int entityId)
			{
			SpawnerComp s=spawner.get(spawnerId);
			s.current_count--;
			}
		}
	}
