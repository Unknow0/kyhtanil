package unknow.kyhtanil.server.component;

import unknow.kyhtanil.server.pojo.*;

import com.artemis.*;

public class EquipementComp extends PooledComponent
	{
	public Armor body;
	public Armor helm;
	public Armor glove;
	public Armor boots;
	public Weapon mainHand;
	public Item offHand;

	protected void reset()
		{
		}
	}
