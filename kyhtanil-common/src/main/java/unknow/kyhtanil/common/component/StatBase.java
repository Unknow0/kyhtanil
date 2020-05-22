package unknow.kyhtanil.common.component;

import com.artemis.PooledComponent;

/**
 * base stats (without modifier)
 */
public class StatBase extends PooledComponent implements Setable<StatBase> {
	/** the strength */
	public int strength;
	/** the constitution */
	public int constitution;
	/** the intelligence */
	public int intelligence;
	/** the concentration */
	public int concentration;
	/** the dexterity */
	public int dexterity;

	/** the current level */
	public int level;
	/** experience point */
	public int xp;

	@Override
	protected void reset() {
	}

	@Override
	public Class<StatBase> component() {
		return StatBase.class;
	}

	@Override
	public void setTo(StatBase t) {
		t.strength = strength;
		t.constitution = constitution;
		t.intelligence = intelligence;
		t.concentration = concentration;
		t.dexterity = dexterity;
		t.xp = xp;
		t.level = level;
	}

	/**
	 * cost of the level
	 * 
	 * @param i level
	 * @return the cost of level
	 */
	public static int cost(int i) {
		return 10 + i;
	}

	/**
	 * combined cost to go from start to end
	 * 
	 * @param start
	 * @param end
	 * @return total cost
	 */
	public static int costdiff(int start, int end) {
		int n = end - start;
		return (n + 1) * (cost(start) + cost(end)) / 2 - cost(start);
	}

	/**
	 * maximum reachable level
	 * 
	 * @param level body level
	 * @param start stat level
	 * @param xp    actual xp
	 * @return max level
	 */
	public static int max(int level, int start, int xp) {
		int e = xp;
		int i = start;
		int c = cost(level);
		while (e > c) {
			e -= c;
			c += 1;
			i++;
		}
		return i;
	}

	@Override
	public String toString() {
		return "StatBase [strength=" + strength + ", constitution=" + constitution + ", intelligence=" + intelligence + ", concentration=" + concentration + ", dexterity=" + dexterity + ", level=" + level + ", xp=" + xp + "]";
	}
}
