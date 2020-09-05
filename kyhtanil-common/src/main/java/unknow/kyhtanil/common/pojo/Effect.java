/**
 * 
 */
package unknow.kyhtanil.common.pojo;

import unknow.kyhtanil.common.Stats;

/**
 * @author unknow
 */
public class Effect {
	private Stats stat;
	private int value;

	public Stats getStat() {
		return stat;
	}

	public void setStat(Stats stat) {
		this.stat = stat;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Effect [stat=" + stat + ", value=" + value + "]";
	}
}
