package unknow.kyhtanil.server.component;

import unknow.kyhtanil.server.component.DamageListComp.*;

import com.artemis.*;

public class CalculatedComp extends PooledComponent
	{
	public Damage dmg;
	// TODO res

	public int maxHp;
	public int maxMp;

	protected void reset()
		{
		dmg=null;
		}
	}
