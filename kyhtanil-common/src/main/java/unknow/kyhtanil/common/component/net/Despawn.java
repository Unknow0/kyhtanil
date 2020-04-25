package unknow.kyhtanil.common.component.net;

import unknow.kyhtanil.common.pojo.*;

import com.artemis.*;

public class Despawn extends Component {
	public UUID uuid = null;

	public Despawn() {
	}

	public Despawn(UUID uuid) {
		this.uuid = uuid;
	}

	@Override
	public String toString() {
		return "Despawn [uuid=" + uuid + "]";
	}
}