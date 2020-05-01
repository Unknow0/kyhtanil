package unknow.kyhtanil.server.system;

import java.util.function.IntPredicate;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.artemis.utils.IntBag;

import unknow.kyhtanil.common.component.Position;
import unknow.kyhtanil.common.component.StatShared;
import unknow.kyhtanil.server.component.Projectile;
import unknow.kyhtanil.server.manager.LocalizedManager;
import unknow.kyhtanil.server.manager.UUIDManager;

/**
 * manage projectile life time & on hit effect
 * 
 * @author unknow
 */
public class ProjectileSystem extends IteratingSystem {
	private LocalizedManager locManager;
	private UUIDManager uuid;
	private ComponentMapper<Projectile> projectile;
	private ComponentMapper<Position> position;
	private ComponentMapper<StatShared> mobInfo;
	private IntPredicate c = e -> mobInfo.has(e);

	/**
	 * create new ProjectileSystem
	 */
	public ProjectileSystem() {
		super(Aspect.all(Projectile.class, Position.class));
	}

	@Override
	protected void process(int e) {
		Projectile proj = projectile.get(e);
		Position p = position.get(e);

		Integer src = uuid.getEntity(proj.source);
		if (src == null) {
			world.delete(e);
			return;
		}

		IntBag potential = locManager.get(p.x, p.y, 50, c); // TODO width & collision
		for (int i = 0; i < potential.size(); i++) {
			int t = potential.get(i);
			if (t == src.intValue())
				continue;
			if (p.distance(position.get(t)) < 16) {
				if (proj.onHit != null)
					proj.onHit.accept(t);
				world.delete(e);
				return;
			}
		}
	}
}
