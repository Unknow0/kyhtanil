/**
 * 
 */
package unknow.kyhtanil.common.pojo;

import java.util.Arrays;

/**
 * concrete item
 * 
 * @author unknow
 */
public class Item {
	private int base;
	private Effect[] stats;

	public int getBase() {
		return base;
	}

	public void setBase(int base) {
		this.base = base;
	}

	public Effect[] getStats() {
		return stats;
	}

	public void setStats(Effect[] stats) {
		this.stats = stats;
	}

	@Override
	public String toString() {
		return "Item [base=" + base + ", stats=" + Arrays.toString(stats) + "]";
	}
}
