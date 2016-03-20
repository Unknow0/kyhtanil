package unknow.kyhtanil.common.component;

import com.artemis.*;

public class Body extends PooledComponent
	{
	public transient int id;

	/* basic stat */
	public int strength;
	public int constitution;
	public int intelligence;
	public int concentration;
	public int dexterity;

	public long xp;
	public int level;
	public int points;

	protected void reset()
		{
		}
	}
