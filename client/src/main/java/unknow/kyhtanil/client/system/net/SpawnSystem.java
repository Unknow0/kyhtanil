package unknow.kyhtanil.client.system.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

import unknow.kyhtanil.client.component.Archetypes;
import unknow.kyhtanil.common.component.Position;
import unknow.kyhtanil.common.component.Sprite;
import unknow.kyhtanil.common.component.StatShared;
import unknow.kyhtanil.common.component.Velocity;
import unknow.kyhtanil.common.component.net.Spawn;
import unknow.kyhtanil.common.util.BaseUUIDManager;

/**
 * apply spawn event
 * 
 * @author unknow
 */
public class SpawnSystem extends IteratingSystem {
	private static final Logger log = LoggerFactory.getLogger(SpawnSystem.class);
	private ComponentMapper<Spawn> spawn;
	private ComponentMapper<StatShared> info;
	private ComponentMapper<Position> position;
	private ComponentMapper<Velocity> velocity;
	private ComponentMapper<Sprite> sprite;
	private BaseUUIDManager manager;
	private Archetypes arch;

	/**
	 * create new SpawnSystem
	 */
	public SpawnSystem() {
		super(Aspect.all(Spawn.class));
	}

	@Override
	protected void process(int entityId) {
		Spawn s = spawn.get(entityId);
		world.delete(entityId);
		log.info("{}", s);

		int mob = world.create(arch.all);
		manager.setUuid(mob, s.uuid);
		if (s.m != null)
			info.get(mob).set(s.m);
		position.get(mob).set(s.p);
		velocity.get(mob).set(s.v);
		sprite.get(mob).set(s.sprite);
	}
}
