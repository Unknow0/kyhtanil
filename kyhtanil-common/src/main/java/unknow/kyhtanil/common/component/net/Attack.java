package unknow.kyhtanil.common.component.net;

import unknow.kyhtanil.common.pojo.*;

import com.artemis.*;

public class Attack extends PooledComponent {
	public UUID uuid=null;
	public int id=0;
	public Object target=null;

	public Attack() {
	}

	public Attack(UUID uuid, int id, Object target) {
		this.uuid=uuid;
		this.id=id;
		this.target=target;
	}

	public void reset() {
		uuid=null;
		id=0;
		target=null;
	}

	public String toString() {
		return "uuid: "+uuid+", id: "+id+", target: "+target;
	}
}