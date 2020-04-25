package unknow.kyhtanil.common.component;

import com.artemis.PooledComponent;

public class StatPoint extends PooledComponent implements Setable<StatPoint> {
	public int base;
	public int exp;

	public StatPoint() {
	}

	public StatPoint(int base) {
		this.base = base;
	}

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
