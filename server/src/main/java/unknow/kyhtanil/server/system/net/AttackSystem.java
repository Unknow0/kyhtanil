package unknow.kyhtanil.server.system.net;

import java.util.function.IntConsumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import com.artemis.utils.IntBag;

import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import unknow.kyhtanil.common.Stats;
import unknow.kyhtanil.common.component.Dirty;
import unknow.kyhtanil.common.component.Position;
import unknow.kyhtanil.common.component.Sprite;
import unknow.kyhtanil.common.component.StatAgg;
import unknow.kyhtanil.common.component.StatShared;
import unknow.kyhtanil.common.component.Velocity;
import unknow.kyhtanil.common.component.net.Attack;
import unknow.kyhtanil.common.pojo.UUID;
import unknow.kyhtanil.server.Database;
import unknow.kyhtanil.server.component.Archetypes;
import unknow.kyhtanil.server.component.Damage;
import unknow.kyhtanil.server.component.NetComp;
import unknow.kyhtanil.server.component.Projectile;
import unknow.kyhtanil.server.component.TTL;
import unknow.kyhtanil.server.manager.LocalizedManager;
import unknow.kyhtanil.server.manager.UUIDManager;

/**
 * manage attack event
 * 
 * @author unknow
 */
public class AttackSystem extends IteratingSystem {
	private static final Logger log = LoggerFactory.getLogger(AttackSystem.class);

	private UUIDManager manager;
	private LocalizedManager locManager;

	private ComponentMapper<Attack> attack;
	private ComponentMapper<NetComp> net;
	private ComponentMapper<Damage> damage;
	private ComponentMapper<TTL> ttl;
	private ComponentMapper<Position> position;
	private ComponentMapper<Velocity> velocity;
	private ComponentMapper<StatShared> mobInfo;
	private ComponentMapper<Dirty> dirty;
	private ComponentMapper<Projectile> projectile;
	private ComponentMapper<Sprite> sprite;
	private ComponentMapper<StatAgg> stat;

	private Archetypes arch;

	@Wire
	private Database database;

	private IntObjectMap<Skill> skills = new IntObjectHashMap<>();

	/**
	 * create new AttackSystem
	 */
	public AttackSystem() {
		super(Aspect.all(Attack.class, NetComp.class));
	}

	@Override
	protected void initialize() {
		// auto attack
		skills.put(0, (self, point, target) -> {
			Position p = position.get(self);
			Sprite sp = sprite.get(self);
			double r = Math.atan2(point.y - p.y, point.x - p.x);

			// TODO weapon range
			IntBag intBag = locManager.get(p.x, p.y, 32, e -> p.distance(position.get(e)) < sp.w + sprite.get(e).w);
			if (intBag.isEmpty())
				return;
			for (int i = 0; i < intBag.size(); i++) {
				int t = intBag.get(i);
				if (t == self)
					continue;
				Position m = position.get(t);
				double mr = Math.atan2(m.y - p.y, m.x - p.x);
				double diff = r - mr;
				if (-1 < diff && diff < 1) {
					target = t;
					break;
				}
			}

			// TODO proj for ranged weapon
			if (target != null) {
				StatAgg s = stat.get(self);
				addDamage(self, s.get(Stats.WPN_DMG_SLASH), s.get(Stats.WPN_DMG_BLUNT), s.get(Stats.WPN_DMG_PIERCE), s.get(Stats.WPN_DMG_LIGTHNING), s.get(Stats.WPN_DMG_FIRE), s.get(Stats.WPN_DMG_ICE), 0, target);
			}
		});

		// fireball
		skills.put(1, (self, point, target) -> {
			int cost = 1; // TODO
			StatShared info = mobInfo.get(self);
			if (info.mp < cost)
				return;
			info.mp -= cost;
			dirty.get(self).add(info);

			Position p = position.get(self);
			float r = (float) Math.atan2(point.y - p.y, point.x - p.x);

			addProj(self, r, 25, 5, "skills/fire", t -> {
				Position p2 = position.get(t);

				IntBag intBag = locManager.get(p2.x, p2.y, 50, null);
				for (int i = 0; i < intBag.size(); i++) {
					t = intBag.get(i);
					if (t != self)
						addDamage(self, 0, 0, 0, 0, 5, 0, 0, t);
				}
			});
		});
	}

	@Override
	protected void process(int entityId) {
		Attack a = attack.get(entityId);
		NetComp ctx = net.get(entityId);

		world.delete(entityId);
		Integer self = manager.getEntity(a.uuid);
		if (self == null) {
			ctx.channel.close();
			log.debug("failed to get State '{}' on attack", a.uuid);
			return;
		}

		Integer t = null;
		Position p;
		if (a.target instanceof UUID) {
			t = manager.getEntity((UUID) a.target);
			if (t == null)
				return;
			p = position.get(t);
		} else
			p = (Position) a.target;

		Skill script = skills.get(a.id);

		script.exec(self, p, t);
	}

	private void addDamage(int source, int slashing, int blunt, int pierce, int lightning, int fire, int ice, float duration, int target) {
		int e = world.create(arch.damage);
		Damage d = damage.get(e);
		d.source = source;
		d.target = target;

		d.slashing = slashing;
		d.blunt = blunt;
		d.piercing = pierce;
		d.lightning = lightning;
		d.fire = fire;
		d.ice = ice;

		ttl.get(e).ttl = duration;
	}

	private void addProj(int source, float dir, float speed, float ttl, String tex, IntConsumer onHit) {
		final int e = world.create(arch.proj);
		position.get(e).set(position.get(source));
		Velocity v = velocity.get(e);
		v.direction = dir;
		v.speed = speed;
		this.ttl.get(e).ttl = ttl;
		Projectile p = projectile.get(e);
		p.onHit = onHit;
		p.source = manager.getUuid(source);

		Sprite s = sprite.get(e);
		s.w = s.h = 8;
		s.rotation = dir;
		s.tex = tex;
	}

	private interface Skill {
		void exec(int source, Position p, Integer target);
	}
}
