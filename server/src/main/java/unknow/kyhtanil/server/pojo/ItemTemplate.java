/**
 * 
 */
package unknow.kyhtanil.server.pojo;

import unknow.kyhtanil.common.Stats;
import unknow.kyhtanil.common.pojo.Item;

/**
 * represent an item
 * 
 * @author unknow
 */
public class ItemTemplate {
	private int id;
	private StatTemplate[] stats;

	/**
	 * generate an item
	 * 
	 * @return
	 */
	public Item gen() {
		Item item = new Item();
		item.setBase(id);
		// TODO generate effect
		for (int i = 0; i < stats.length; i++) {
		}
		return item;
	}

	/**
	 * Stat template
	 * 
	 * @author unknow
	 */
	public static class StatTemplate {
		private double rate;
		private Stats stat;
		private int min;
		private int max;

		public double getRate() {
			return rate;
		}

		public void setRate(double rate) {
			this.rate = rate;
		}

		public Stats getStat() {
			return stat;
		}

		public void setStat(Stats stat) {
			this.stat = stat;
		}

		public int getMin() {
			return min;
		}

		public void setMin(int min) {
			this.min = min;
		}

		public int getMax() {
			return max;
		}

		public void setMax(int max) {
			this.max = max;
		}
	}
}
