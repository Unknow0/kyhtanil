package unknow.kyhtanil.common.component.account;

import com.artemis.PooledComponent;
import javax.annotation.Generated;
import unknow.kyhtanil.common.component.Body;
import unknow.kyhtanil.common.component.CalculatedComp;

@Generated(value="class unknow.pojo.Generator", date="2017-05-01T19:13:39+0200")
public class PjInfo extends PooledComponent {
	public String name=null;
	public float x=0;
	public float y=0;
	public int hp=0;
	public int mp=0;
	public Body body=null;
	public CalculatedComp total=null;

	public PjInfo() {
	}

	public PjInfo(String name, float x, float y, int hp, int mp, Body body, CalculatedComp total) {
		this.name=name;
		this.x=x;
		this.y=y;
		this.hp=hp;
		this.mp=mp;
		this.body=body;
		this.total=total;
	}

	public void reset() {
		name=null;
		x=0;
		y=0;
		hp=0;
		mp=0;
		body=null;
		total=null;
	}

	public String toString() {
		return "name: "+name+", x: "+x+", y: "+y+", hp: "+hp+", mp: "+mp+", body: "+body+", total: "+total;
	}
}