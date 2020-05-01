package unknow.kyhtanil.common.component;

import com.artemis.PooledComponent;

/**
 * Hold exp & stats points
 * 
 * @author unknow
 */
public class StatPoint extends PooledComponent implements Setable<StatPoint> {
	/** points to update base stats */
	public int base;
	/** collected xp */
	public int exp;

	@Override
	protected void reset() {
		base = exp = 0;
	}

	@Override
	public void set(StatPoint t) {
		base = t.base;
		exp = t.exp;
	}

	@Override
	public String toString() {
		return "StatPoint [base=" + base + ", exp=" + exp + "]";
	}
}
