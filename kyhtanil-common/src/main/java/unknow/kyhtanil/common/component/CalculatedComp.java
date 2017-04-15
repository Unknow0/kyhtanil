package unknow.kyhtanil.common.component;

import unknow.kyhtanil.common.pojo.*;

import com.artemis.*;

public class CalculatedComp extends PooledComponent
	{
	public Damage dmg=new Damage();
	// TODO res

	public int hp;
	public int mp;

	public int maxHp;
	public int maxMp;

	public int strength;
	public int constitution;
	public int intelligence;
	public int concentration;
	public int dexterity;

	public float moveSpeed;

	public void reset()
		{
		dmg=new Damage();
		}

	public void set(CalculatedComp c)
		{
		this.dmg=c.dmg;

		this.hp=c.hp;
		this.mp=c.mp;
		this.maxHp=c.maxHp;
		this.maxMp=c.maxMp;

		this.strength=c.strength;
		this.constitution=c.constitution;
		this.intelligence=c.intelligence;
		this.concentration=c.concentration;
		this.dexterity=c.dexterity;

		this.moveSpeed=c.moveSpeed;
		}
	}
