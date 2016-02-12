package unknow.kyhtanil.server.component;

import com.artemis.*;

public class MobInfoComp extends PooledComponent
	{
	public String name;
	public int level;
	public int hp;
	public int mp;
	public int constitution;
	public int strength;
	public int concentration;
	public int intelligence;
	public int dexterity;

	protected void reset()
		{
		name=null;
		level=0;
		hp=mp=0;
		constitution=strength=concentration=intelligence=dexterity=0;
		}
	}
