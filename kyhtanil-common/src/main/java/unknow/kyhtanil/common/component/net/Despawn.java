package unknow.kyhtanil.common.component.net;

import unknow.kyhtanil.common.pojo.*;

import com.artemis.*;

public class Despawn extends PooledComponent {
	public UUID uuid=null;

	public Despawn() {
	}

	public Despawn(UUID uuid) {
		this.uuid=uuid;
	}

	public void reset() {
		uuid=null;
	}

	public String toString() {
		return "uuid: "+uuid;
	}
}