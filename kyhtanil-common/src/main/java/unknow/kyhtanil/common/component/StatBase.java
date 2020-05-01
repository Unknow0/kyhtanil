package unknow.kyhtanil.common.component;

import com.artemis.PooledComponent;

import unknow.kyhtanil.common.pojo.Damage;

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

	/** the level TODO move to StatPoints? */
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
