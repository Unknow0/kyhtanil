package unknow.kyhtanil.common.component;

import com.artemis.PooledComponent;

import unknow.kyhtanil.common.pojo.Damage;

/**
 * base stats (without modifier)
 */
public class StatBase extends PooledComponent implements Setable<StatBase> {
	public int strength;
	public int constitution;
	public int intelligence;
	public int concentration;
	public int dexterity;

	public int level;

	/** weapon damage */
	public Damage dmg = new Damage();

	@Override
	protected void reset() {
		dmg.reset();
	}

	@Override
	public void set(StatBase t) {
		strength = t.strength;
		constitution = t.constitution;
		intelligence = t.intelligence;
		concentration = t.concentration;
		dexterity = t.dexterity;
		dmg = t.dmg;
	}

	@Override
	public String toString() {
		return "StatBase [strength=" + strength + ", constitution=" + constitution + ", intelligence=" + intelligence + ", concentration=" + concentration + ", dexterity=" + dexterity + ", level=" + level + ", dmg=" + dmg + "]";
	}
}
