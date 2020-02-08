package unknow.kyhtanil.server.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

import unknow.kyhtanil.common.component.PositionComp;
import unknow.kyhtanil.common.component.SpriteComp;
import unknow.kyhtanil.common.component.StatBase;
import unknow.kyhtanil.common.component.StatShared;
import unknow.kyhtanil.common.component.VelocityComp;
import unknow.kyhtanil.server.component.Archetypes;
import unknow.kyhtanil.server.component.SpawnerComp;

public class SpawnSystem extends IteratingSystem {
	private ComponentMapper<SpawnerComp> spawner;
	private ComponentMapper<PositionComp> position;
	private ComponentMapper<VelocityComp> velocity;
	private ComponentMapper<StatShared> mobInfo;
	private ComponentMapper<SpriteComp> sprite;
	private ComponentMapper<StatBase> stats;

	private Archetypes arch;

	private UpdateStatSystem update;
	private EventSystem event;

	public SpawnSystem() {
		super(Aspect.all(SpawnerComp.class));
	}

	@Override
	protected void process(int e) {
		SpawnerComp s = spawner.get(e);
		if (s.current_count > s.max_count)
			return;
		s.current += world.delta * s.creation_speed;

		if (s.current > 1) {
			s.current = 0;
			s.current_count++;
			int m = world.create(arch.mob);

			PositionComp p = position.get(m);
			p.x = (float) (s.x + Math.random() * s.range * 2 - s.range);
			p.y = (float) (s.y + Math.random() * s.range * 2 - s.range);

			VelocityComp v = velocity.get(m);
			v.direction = 0;
			v.speed = 0f;

			StatShared mi = mobInfo.get(m);
			mi.hp = 10;
			mi.name = "mob"; // TODO

			StatBase st = stats.get(m);
			// TODO load mob stats

			SpriteComp sp = sprite.get(m);
			sp.h = sp.w = 3;
			sp.tex = "data/tex/mob.png";

			update.process(m); // XXX ??
			event.register(m, new Listener(spawner, e));
		}
	}

	private static class Listener implements EventSystem.EntityListener {
		private ComponentMapper<SpawnerComp> spawner;
		private int spawnerId;

		public Listener(ComponentMapper<SpawnerComp> spawner, int spawnerId) {
			this.spawner = spawner;
			this.spawnerId = spawnerId;
		}

		@Override
		public void inserted(int entityId) {
		}

		@Override
		public void removed(int entityId) {
			SpawnerComp s = spawner.get(spawnerId);
			s.current_count--;
		}
	}
}
