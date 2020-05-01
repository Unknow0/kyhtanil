package unknow.kyhtanil.server.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.IntPredicate;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.utils.IntMap;

import unknow.common.data.IntArraySet;
import unknow.kyhtanil.common.component.Position;
import unknow.kyhtanil.server.component.StateComp;
import unknow.kyhtanil.server.component.StateComp.States;

/**
 * manage entities by location by spliting the world be sub square
 * 
 * @author unknow
 */
public class LocalizedManager extends BaseEntitySystem {
	private Map<Loc, IntBag> locMap = new HashMap<>();
	private IntMap<Loc> objects = new IntMap<>();

	private float w, h;

	private ComponentMapper<Position> position;
	private ComponentMapper<StateComp> state;

	private List<Area> areas = new ArrayList<>();
	private IntArraySet changed = new IntArraySet(16);

	/**
	 * create a manager with a w x h square to store entity
	 * 
	 * @param w width of sub square
	 * @param h height of sub square
	 */
	public LocalizedManager(float w, float h) {
		super(Aspect.all(Position.class));
		this.w = w;
		this.h = h;
	}

	@Override
	public void inserted(int entityId) {
		Position p = position.get(entityId);
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
			Position sp = position.get(a.source);
			if (sp.distance(p) <= a.range) {
				a.inside.add(entityId);
				a.listener.enter(entityId);
			}
		}
	}

	/**
	 * notify an entity have moved
	 * 
	 * @param entityId entity taht moved
	 */
	public void changed(int entityId) {
		Loc loc = objects.get(entityId);
		IntBag bag = locMap.get(loc);

		Position p = position.get(entityId);

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
			Position sp = position.get(a.source);
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
					Position p = position.get(i);
					if (p.distance(sp) > a.range) {
						a.inside.remove(i);
						a.listener.leave(i);
					}
				}
			}
		}
		changed.clear();
	}

	/**
	 * get all entity in circle
	 * 
	 * @param x center of circle
	 * @param y center of circle
	 * @param r radius of circle
	 * @param c filter to the found entities
	 * @return all entities in circle that match
	 */
	public IntBag get(float x, float y, float r, IntPredicate c) {
		float mx = x + 2 * r;
		float my = y + 2 * r;
		IntBag bag = new IntBag();
		for (float cx = (x - r - (x - r) % w); cx <= mx; cx += w) {
			for (float cy = y - r - (y - r) % h; cy <= my; cy += h) {
				IntBag l = locMap.get(new Loc(cx, cy));
				if (l != null) {
					loop: for (int i = 0; i < l.size(); i++) {
						int e = l.get(i);
						Position p = position.get(e);
						if (p == null)
							continue;
						if (c != null && !c.test(e))
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

	/**
	 * track an area around an entity
	 * 
	 * @param source   center of the area
	 * @param range    range of the area
	 * @param listener the listener
	 */
	public void track(int source, float range, AreaListener listener) {
		Area a = new Area(source, range, listener);
		areas.add(a);
		Position sp = position.get(source);

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

	/**
	 * listener for area tracking
	 * 
	 * @author unknow
	 */
	public static interface AreaListener {
		/**
		 * notify that an entity entered the area
		 * 
		 * @param target the entity that entered
		 */
		void enter(int target);

		/**
		 * notify that an entity leaved the area
		 * 
		 * @param target the entity that leaved
		 */
		void leave(int target);
	}

	/**
	 * a tracked area
	 * 
	 * @author unknow
	 */
	private static class Area {
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
