package unknow.kyhtanil.common.component.net;

import com.artemis.PooledComponent;

import unknow.kyhtanil.common.component.CalculatedComp;
import unknow.kyhtanil.common.component.MobInfoComp;
import unknow.kyhtanil.common.component.PositionComp;
import unknow.kyhtanil.common.pojo.UUID;

public class UpdateInfo extends PooledComponent
	{
	public UUID uuid=null;
	public float x=0;
	public float y=0;
	public float direction=0;
	public int hp, maxHp;
	public int mp, maxMp;

	public UpdateInfo()
		{
		}

	public UpdateInfo(UUID uuid, PositionComp p, CalculatedComp m)
		{
		this.uuid=uuid;
		this.x=p.x;
		this.y=p.y;
		this.direction=0;
		this.hp=m.hp;
		this.mp=m.mp;
		this.maxHp=m.maxHp;
		this.maxMp=m.maxMp;
		}

	public void reset()
		{
		uuid=null;
		x=y=0;
		direction=0;
		hp=maxHp=0;
		mp=maxMp=0;
		}

	public String toString()
		{
		return "uuid: "+uuid+", x: "+x+", y: "+y+", direction: "+direction;
		}
	}
