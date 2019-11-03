package unknow.kyhtanil.common.component.account;

import com.artemis.Component;

import unknow.kyhtanil.common.component.Body;
import unknow.kyhtanil.common.component.StatPerso;
import unknow.kyhtanil.common.component.StatShared;

public class PjInfo extends Component
	{
	public String name=null;
	public float x=0;
	public float y=0;
	public Body body=null;
	public StatShared stats=null;
	public StatPerso perso=null;

	public PjInfo()
		{
		}

	public PjInfo(String name, float x, float y, Body body, StatShared stats, StatPerso perso)
		{
		this.name=name;
		this.x=x;
		this.y=y;
		this.body=body;
		this.stats=stats;
		this.perso=perso;
		}

	public String toString()
		{
		return "name: "+name+", x: "+x+", y: "+y+", body: "+body+", total: "+stats;
		}
	}