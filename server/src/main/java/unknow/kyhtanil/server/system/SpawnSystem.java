package unknow.kyhtanil.server.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

import unknow.kyhtanil.common.component.Position;
import unknow.kyhtanil.common.component.Sprite;
import unknow.kyhtanil.common.component.StatBase;
import unknow.kyhtanil.common.component.StatShared;
import unknow.kyhtanil.common.component.Velocity;
import unknow.kyhtanil.server.Database;
import unknow.kyhtanil.server.component.Archetypes;
import unknow.kyhtanil.server.component.Spawned;
import unknow.kyhtanil.server.component.Spawner;
import unknow.kyhtanil.server.pojo.IdRate;
import unknow.kyhtanil.server.pojo.Mob;

/**
 * spawn new entities
 * 
 * @author unknow
 */
public class SpawnSystem extends IteratingSystem {
	private ComponentMapper<Spawner> spawner;
	private ComponentMapper<Spawned> spawned;
	private ComponentMapper<Position> position;
	private ComponentMapper<Velocity> velocity;
	private ComponentMapper<StatShared> mobInfo;
	private ComponentMapper<Sprite> sprite;
	private ComponentMapper<StatBase> stats;

	private Archetypes arch;

	private UpdateStatSystem update;
	private Database database;

	/**
	 * create new SpawnSystem
	 */
	public SpawnSystem() {
		super(Aspect.all(Spawner.class));
	}

	@Override
	protected void process(int e) {
		Spawner s = spawner.get(e);
		if (s.count > s.max)
			return;
		s.time += world.delta * s.speed;

		if (s.time > 1) {
			s.time = 0;
			s.count++;
			int m = world.create(arch.managerMob);

			Mob mob = database.getMob(IdRate.gen(s.mobs));
			Spawned spa = spawned.get(m);
			spa.spawner = e;
			spa.mob = mob;

			Position p = position.get(m);
			p.x = (float) (s.x + Math.random() * s.r * 2 - s.r);
			p.y = (float) (s.y + Math.random() * s.r * 2 - s.r);

			Velocity v = velocity.get(m);
			v.direction = (float) (Math.random() * Math.PI * 2 - Math.PI);
			v.speed = 0;

			StatShared mi = mobInfo.get(m);
			mi.name = mob.name;

			StatBase st = stats.get(m);
			st.strength = mob.strength;
			st.constitution = mob.constitution;
			st.intelligence = mob.intelligence;
			st.concentration = mob.concentration;
			st.dexterity = mob.dexterity;

			Sprite sp = sprite.get(m);
			sp.h = sp.w = mob.w;
			sp.tex = mob.tex;

			update.process(m);

			mi.hp = mi.maxHp;
			mi.mp = mi.maxMp;
		}
	}
}
