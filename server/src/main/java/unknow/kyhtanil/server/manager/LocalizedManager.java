package unknow.kyhtanil.server.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.utils.IntMap;

import unknow.common.data.IntArraySet;
import unknow.kyhtanil.common.component.PositionComp;
import unknow.kyhtanil.server.component.StateComp;
import unknow.kyhtanil.server.component.StateComp.States;

public class LocalizedManager extends BaseEntitySystem {
	private Map<Loc, IntBag> locMap = new HashMap<>();
	private IntMap<Loc> objects = new IntMap<>();

	private float w, h;

	private ComponentMapper<PositionComp> position;
	private ComponentMapper<StateComp> state;

	private List<Area> areas = new ArrayList<>();
	private IntArraySet changed = new IntArraySet(16);

	/**
	 * create a manager with a w x h square to store entity
	 */
	public LocalizedManager(float w, float h) {
		super(Aspect.all(PositionComp.class));
		this.w = w;
		this.h = h;
	}

	@Override
	public void inserted(int entityId) {
		PositionComp p = position.get(entityId);
		if (p == null)
			return;
		// don't add pj at there creation
		StateComp s = state.get(entityId);
		if (s != null && s.state != States.IN_GAME)
			return;

		Loc loc = new Loc(p.x - p.x % w, p.y - p.y % h);
		IntBag bag = locMap.get(loc);
		if (bag == null) {
			bag = new IntBag();
			locMap.put(loc, bag);
		}
		bag.add(entityId);
		objects.put(entityId, loc);

		for (Area a : areas) {
			if (a.source == entityId)
				continue;
			PositionComp sp = position.get(a.source);
			if (sp.distance(p) <= a.range) {
				a.inside.add(entityId);
				a.listener.enter(entityId);
			}
		}
	}

	public void changed(int entityId) {
		Loc loc = objects.get(entityId);
		IntBag bag = locMap.get(loc);

		PositionComp p = position.get(entityId);

		if (p == null) {
			bag.remove(entityId);
			objects.remove(entityId);
			return;
		}

		Loc nloc = new Loc(p.x - p.x % w, p.y - p.y % h);
		if (nloc.x == loc.x && nloc.y == loc.y)
			return;

		bag.removeValue(entityId);

		bag = locMap.get(nloc);
		if (bag == null) {
			bag = new IntBag();
			locMap.put(nloc, bag);
		}
		bag.add(entityId);
		objects.put(entityId, nloc);

		changed.add(entityId);
	}

	@Override
	public void removed(int entityId) {
		Loc loc = objects.remove(entityId);
		if (loc == null)
			return;
		IntBag bag = locMap.get(loc);
		bag.removeValue(entityId);

		Iterator<Area> it = areas.iterator();
		while (it.hasNext()) {
			Area a = it.next();
			if (a.source == entityId) {
				it.remove();
				continue;
			}
			if (a.inside.remove(entityId))
				a.listener.leave(entityId);
		}
	}

	@Override
	protected void processSystem() {
		for (Entry<Loc, IntBag> e : locMap.entrySet()) {
			Loc key = e.getKey();
			IntBag bag = e.getValue();
			int[] data = bag.getData();
			for (int i = 0; i < bag.size(); i++) {
				if (!key.equals(objects.get(data[i])))
					throw new RuntimeException("inconsistancy");
			}
		}

		for (Area a : areas) {
			PositionComp sp = position.get(a.source);
			if (changed.contains(a.source)) {
				IntBag bag = get(sp.x, sp.y, a.range, null);
				IntArraySet entities = new IntArraySet(bag.getData(), 0, bag.size());
				entities.remove(a.source);
				Iterator<Integer> it = a.inside.iterator();
				while (it.hasNext()) {
					Integer e = it.next();
					if (!entities.contains(e)) {
						a.listener.leave(e);
						it.remove();
					}
				}
				for (Integer e : entities) {
					if (!a.inside.contains(e))
						a.listener.enter(e);
				}
				a.inside.addAll(entities);
			} else {
				for (Integer i : changed) {
					if (!a.inside.contains(i))
						continue;
					PositionComp p = position.get(i);
					if (p.distance(sp) > a.range) {
						a.inside.remove(i);
						a.listener.leave(i);
					}
				}
			}
		}
		changed.clear();
	}

	public IntBag get(float x, float y, float r, Choose c) {
		float mx = x + 2 * r;
		float my = y + 2 * r;
		IntBag bag = new IntBag();
		for (float cx = (x - r - (x - r) % w); cx <= mx; cx += w) {
			for (float cy = y - r - (y - r) % h; cy <= my; cy += h) {
				IntBag l = locMap.get(new Loc(cx, cy));
				if (l != null) {
					loop: for (int i = 0; i < l.size(); i++) {
						int e = l.get(i);
						PositionComp p = position.get(e);
						if (p == null)
							continue;
						if (c != null && !c.choose(e))
							continue loop;

						float dx = x - p.x;
						float dy = y - p.y;
						double dist = Math.sqrt(dx * dx + dy * dy);
						if (dist <= r)
							bag.add(e);
					}
				}
			}
		}
		return bag;
	}

	public void track(int source, float range, AreaListener listener) {
		Area a = new Area(source, range, listener);
		areas.add(a);
		PositionComp sp = position.get(source);

		IntBag intBag = get(sp.x, sp.y, range, null);
		for (int i = 0; i < intBag.size(); i++) {
			int e = intBag.get(i);
			if (e == source)
				continue;
			a.inside.add(e);
			listener.enter(e);
		}
	}

	private static final class Loc implements Comparable<Loc> {
		private float x, y;

		public Loc(float x, float y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public int compareTo(Loc l) {
			float cmp = l.x - x;
			if (cmp == 0)
				cmp = l.y - y;
			return (int) cmp;
		}

		@Override
		public boolean equals(Object o) {
			if (o == this)
				return true;
			if (!(o instanceof Loc))
				return false;
			Loc l = (Loc) o;
			return l.x == x && l.y == y;
		}

		@Override
		public int hashCode() {
			return Float.floatToRawIntBits(31 * x + y);
		}

		@Override
		public String toString() {
			return x + "x" + y;
		}
	}

	public static interface Choose {
		boolean choose(int e);
	}

	public static interface AreaListener {
		void enter(int target);

		void leave(int target);
	}

	public static class Area {
		int source;
		float range;
		IntArraySet inside;
		AreaListener listener;

		public Area(int source, float range, AreaListener listener) {
			this.source = source;
			this.range = range;
			this.listener = listener;
			this.inside = new IntArraySet(16);
		}
	}
}
