package unknow.kyhtanil.server;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.artemis.utils.IntBag;

import unknow.kyhtanil.common.Skill;
import unknow.kyhtanil.common.component.PositionComp;
import unknow.kyhtanil.common.component.SpriteComp;
import unknow.kyhtanil.common.component.StatBase;
import unknow.kyhtanil.common.component.VelocityComp;
import unknow.kyhtanil.common.pojo.Damage;
import unknow.kyhtanil.server.component.Archetypes;
import unknow.kyhtanil.server.component.DamageListComp;
import unknow.kyhtanil.server.component.Projectile;
import unknow.kyhtanil.server.manager.LocalizedManager;
import unknow.kyhtanil.server.manager.UUIDManager;
import unknow.kyhtanil.server.utils.Event;

public class ApiWorld {
	private World world;

	private ScriptEngine js = new ScriptEngineManager().getEngineByName("javascript");

	private LocalizedManager locManager;
	private UUIDManager uuid;
	private Archetypes arch;

	private ComponentMapper<DamageListComp> damage;
	private ComponentMapper<StatBase> stat;
	private ComponentMapper<PositionComp> pos;
	private ComponentMapper<VelocityComp> velocity;
	private ComponentMapper<Projectile> proj;
	private ComponentMapper<SpriteComp> sprite;

	public ApiWorld() throws ScriptException {
		js.put("api", this);

		js.put("Skill", js.eval("Java.type('" + Skill.class.getName() + "');"));
		js.put("Damage", js.eval("Java.type('" + Damage.class.getName() + "');"));
	}

	public void addDamage(int source, Damage dmg, float duration, int target) {
		DamageListComp d = damage.get(target);
		d.add(new DamageListComp.Damage(source, 0, r(dmg.slashingMin, dmg.slashingRng), r(dmg.bluntMin, dmg.bluntRng), r(dmg.piercingMin, dmg.piercingRng), r(dmg.lightningMin, dmg.lightningRng), r(dmg.fireMin, dmg.fireRng), r(dmg.iceMin, dmg.iceRng), duration));
	}

	public void addProj(int source, float dir, float speed, float ttl, Event onHit) {
		final int e = world.create(arch.proj);
		pos.get(e).set(pos.get(source));
		VelocityComp v = velocity.get(e);
		v.direction = dir;
		v.speed = speed;
		Projectile p = proj.get(e);
		p.ttl = ttl;
		p.onHit = onHit;
		p.source = uuid.getUuid(source);

		SpriteComp s = sprite.get(e);
		s.w = s.h = 1;
		s.rotation = dir;
		s.tex = "data/tex/proj.png";
	}

	public int[] getMobs(PositionComp p, float r) {
		return get(locManager.get(p.x, p.y, r, filter));
	}

	private static double norm(double angle) {
		while (angle > Math.PI)
			angle -= Math.PI;
		while (angle < -Math.PI)
			angle += Math.PI;
		return angle;
	}

	private final LocalizedManager.Choose filter = new LocalizedManager.Choose() {
		@Override
		public boolean choose(int e) {
			return damage.has(e);
		}
	};

	public int[] getMobs(int src, float r, float arc) {
		PositionComp p = pos.get(src);
		VelocityComp v = velocity.get(src);

		double arcS = norm(v.direction - arc / 2);
		double arcE = norm(v.direction + arc / 2);

		IntBag intBag = locManager.get(p.x, p.y, r, null);
		int i = 0;
		while (i < intBag.size()) {
			int t = intBag.get(i);
			PositionComp pt = pos.get(t);
			double a = Math.atan2(pt.y - p.y, pt.x - p.x);
			if (arcS < arcE && (arcS > a || arcE < a) || arcS > arcE && (a > arcS && a < arcE || a < arcS && a > arcE))
				intBag.remove(i);
			else
				i++;
		}
		return get(intBag);
	}

	private static int[] get(IntBag bag) {
		int[] r = new int[bag.size()];
		for (int i = 0; i < bag.size(); i++)
			r[i] = bag.get(i);
		return r;
	}

	public StatBase stat(int source) {
		return stat.get(source);
	}

	public PositionComp position(int source) {
		return pos.get(source);
	}

	private int r(int min, int rng) {
		return min + (int) (Math.random() * rng);
	}

	protected ScriptEngine js() {
		return js;
	}
}
