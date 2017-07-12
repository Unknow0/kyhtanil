package unknow.kyhtanil.common.component.net;

import unknow.kyhtanil.common.component.*;
import unknow.kyhtanil.common.pojo.*;

import com.artemis.*;

public class Spawn extends PooledComponent
	{
	public UUID uuid=null;
	public String type=null;
	public String name=null;
	public CalculatedComp total=null;
	public float x=0;
	public float y=0;
	public float direction=0;

	public Spawn()
		{
		}

	public Spawn(UUID uuid, String type, String name, CalculatedComp total, float x, float y, float direction)
		{
		this.uuid=uuid;
		this.type=type;
		this.name=name;
		this.total=total;
		this.x=x;
		this.y=y;
		this.direction=direction;
		}

	public void reset()
		{
		uuid=null;
		type=null;
		name=null;
		total=null;
		x=0;
		y=0;
		direction=0;
		}

	public String toString()
		{
		return "uuid: "+uuid+", type: "+type+", name: "+name+", total: "+total+", x: "+x+", y: "+y+", direction: "+direction;
		}
	}