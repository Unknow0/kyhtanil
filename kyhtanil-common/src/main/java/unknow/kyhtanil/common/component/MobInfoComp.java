package unknow.kyhtanil.common.component;

import com.artemis.*;

public class MobInfoComp extends PooledComponent
	{
	public String name;
	public int hp, maxHp;
	public int mp, maxMp;

	public MobInfoComp()
		{
		}

	public MobInfoComp(MobInfoComp m, CalculatedComp c)
		{
		this.name=m.name;
		this.hp=c.hp;
		this.mp=c.mp;
		this.maxHp=c.maxHp;
		this.maxMp=c.maxMp;
		}

	protected void reset()
		{
		name=null;
		hp=mp=0;
		maxHp=maxMp=0;
		}

	public void set(MobInfoComp m)
		{
		this.name=m.name;
		this.hp=m.hp;
		this.maxHp=m.maxHp;
		this.mp=m.mp;
		this.maxMp=m.maxMp;
		}
	}
