package unknow.kyhtanil.common.component;

import java.util.Arrays;

import com.artemis.PooledComponent;

import unknow.kyhtanil.common.Stats;

/**
 * Aggregated stat
 * 
 * @author unknow
 */
public class StatAgg extends PooledComponent implements Setable<StatAgg> {
	/** all stat values */
	public int[] stats = new int[Stats.values().length];

	/** default constructor */
	public StatAgg() {
	}

	@Override
	protected void reset() {
		Arrays.fill(stats, 0);
	}

	@Override
	public Class<StatAgg> component() {
		return StatAgg.class;
	}

	@Override
	public void setTo(StatAgg m) {
		m.stats = stats;
	}

	/**
	 * get the value of a stats
	 * 
	 * @param s the stat to get
	 * @return the value
	 */
	public int get(Stats s) {
		return stats[s.ordinal()];
	}

	/**
	 * set the value of a stat
	 * 
	 * @param s     the stat to set
	 * @param total the value
	 */
	public void set(Stats s, int total) {
		stats[s.ordinal()] = total;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("StatAgg [");
		for (int i = 0; i < stats.length; i++, sb.append(", ")) {
			int v = stats[i];
			if (v == 0)
				continue;
			sb.append(Stats.values()[i] + "=" + v);
		}
		sb.append(']');
		return sb.toString();
	}
}
