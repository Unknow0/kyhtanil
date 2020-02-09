package unknow.kyhtanil.common.component;

import java.util.Arrays;

import com.artemis.PooledComponent;

import unknow.kyhtanil.common.Stats;

/**
 * Stat all off visible entity
 * 
 * @author unknow
 */
public class StatAgg extends PooledComponent implements Setable<StatAgg> {
	public int[] stats = new int[Stats.values().length];

	public StatAgg() {
	}

	@Override
	protected void reset() {
		Arrays.fill(stats, 0);
	}

	@Override
	public void set(StatAgg m) {
		stats = m.stats;
	}

	public int get(Stats s) {
		return stats[s.ordinal()];
	}

	public void set(Stats s, int total) {
		stats[s.ordinal()] = total;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < stats.length; i++) {
			int v = stats[i];
			if (v == 0)
				continue;
			sb.append(Stats.values()[i] + ": " + v);
		}
		return sb.toString();
	}
}
