package unknow.kyhtanil.common.component;

import com.artemis.PooledComponent;

/**
 * Stat all off visible entity
 * 
 * @author unknow
 */
public class StatShared extends PooledComponent implements Setable<StatShared> {
	public String name;
	public int hp, maxHp;
	public int mp, maxMp;

	public StatShared() {
	}

	public StatShared(StatShared m) {
		set(m);
	}

	@Override
	protected void reset() {
		name = null;
		hp = mp = 0;
		maxHp = maxMp = 0;
	}

	@Override
	public void set(StatShared m) {
		this.name = m.name;
		this.hp = m.hp;
		this.maxHp = m.maxHp;
		this.mp = m.mp;
		this.maxMp = m.maxMp;
	}

	@Override
	public String toString() {
		return "name: " + name + ", hp: " + hp + "/" + maxHp + ", mp: " + mp + "/" + maxMp;
	}
}
