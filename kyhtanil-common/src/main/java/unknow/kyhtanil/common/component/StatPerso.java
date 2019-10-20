package unknow.kyhtanil.common.component;

import com.artemis.PooledComponent;

import unknow.kyhtanil.common.pojo.Damage;

public class StatPerso extends PooledComponent implements Setable<StatPerso>
	{
	public int strength;
	public int constitution;
	public int intelligence;
	public int concentration;
	public int dexterity;

	public int moveSpeed;

	/** weapon damage */
	public Damage dmg=new Damage();

	@Override
	protected void reset()
		{
		dmg.reset();
		}

	@Override
	public void set(StatPerso t)
		{
		strength=t.strength;
		constitution=t.constitution;
		intelligence=t.intelligence;
		concentration=t.concentration;
		dexterity=t.dexterity;
		moveSpeed=t.moveSpeed;
		dmg=t.dmg;
		}
	}
