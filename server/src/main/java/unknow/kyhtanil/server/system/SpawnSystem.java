package unknow.kyhtanil.server.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

import unknow.kyhtanil.common.component.Position;
import unknow.kyhtanil.common.component.Sprite;
import unknow.kyhtanil.common.component.StatBase;
import unknow.kyhtanil.common.component.StatShared;
import unknow.kyhtanil.common.component.Velocity;
import unknow.kyhtanil.server.component.Archetypes;
import unknow.kyhtanil.server.component.SpawnerComp;

public class SpawnSystem extends IteratingSystem {
	private ComponentMapper<SpawnerComp> spawner;
	private ComponentMapper<Position> position;
	private ComponentMapper<Velocity> velocity;
	private ComponentMapper<StatShared> mobInfo;
	private ComponentMapper<Sprite> sprite;
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
		if (s.current_count > s.max)
			return;
		s.current += world.delta * s.speed;

		if (s.current > 1) {
			s.current = 0;
			s.current_count++;
			int m = world.create(arch.mob);

			Position p = position.get(m);
			p.x = (float) (s.x + Math.random() * s.r * 2 - s.r);
			p.y = (float) (s.y + Math.random() * s.r * 2 - s.r);

			Velocity v = velocity.get(m);
			v.direction = .5f;
			v.speed = 0;

			StatShared mi = mobInfo.get(m);
			mi.hp = 10;
			mi.name = "mob"; // TODO

			StatBase st = stats.get(m);
			st.concentration = 1;
			// TODO load mob stats

			Sprite sp = sprite.get(m);
			sp.h = sp.w = 16;
			sp.tex = "mob";

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
