package unknow.kyhtanil.common.component;

import com.artemis.PooledComponent;

/**
 * Stat off all visible entity
 * 
 * @author unknow
 */
public class StatShared extends PooledComponent implements Setable<StatShared> {
	/** name of the entity */
	public String name;
	/** the current hp */
	public int hp;
	/** the max hp */
	public int maxHp;
	/** the mp */
	public int mp;
	/** the max mp */
	public int maxMp;

	/**
	 * create new StatShared
	 */
	public StatShared() {
	}

	/**
	 * create new StatShared
	 * 
	 * @param m
	 */
	public StatShared(StatShared m) {
		m.setTo(this);
	}

	@Override
	protected void reset() {
		name = null;
		hp = mp = 0;
		maxHp = maxMp = 0;
	}

	@Override
	public Class<StatShared> component() {
		return StatShared.class;
	}

	@Override
	public void setTo(StatShared m) {
		m.name = this.name;
		m.hp = this.hp;
		m.maxHp = this.maxHp;
		m.mp = this.mp;
		m.maxMp = this.maxMp;
	}

	@Override
	public String toString() {
		return "StatShared [name=" + name + ", hp=" + hp + ", maxHp=" + maxHp + ", mp=" + mp + ", maxMp=" + maxMp + "]";
	}
}
