package unknow.kyhtanil.common.component.net;

import unknow.kyhtanil.common.pojo.*;

import com.artemis.*;

public class Move extends Component {
	public UUID uuid = null;
	public float x = 0;
	public float y = 0;
	public float direction = 0;

	public Move() {
	}

	public Move(UUID uuid, float x, float y, float direction) {
		this.uuid = uuid;
		this.x = x;
		this.y = y;
		this.direction = direction;
	}

	public String toString() {
		return "uuid: " + uuid + ", x: " + x + ", y: " + y + ", direction: " + direction;
	}
}