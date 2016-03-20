package unknow.kyhtanil.server.component;

import com.artemis.*;

public class MobInfoComp extends PooledComponent
	{
	public String name;
	public int hp;
	public int mp;

	protected void reset()
		{
		name=null;
		hp=mp=0;
		}
	}
