package unknow.kyhtanil.common.component.net;

import unknow.kyhtanil.common.pojo.*;

import com.artemis.*;

/**
 * despawn event
 * 
 * @author unknow
 */
public class Despawn extends Component {
	/** the uuid to remove */
	public UUID uuid = null;

	/**
	 * create new Despawn
	 */
	public Despawn() {
	}

	/**
	 * create new Despawn
	 * 
	 * @param uuid
	 */
	public Despawn(UUID uuid) {
		this.uuid = uuid;
	}

	@Override
	public String toString() {
		return "Despawn [uuid=" + uuid + "]";
	}
}