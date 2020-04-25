package unknow.kyhtanil.common.component.net;

import com.artemis.Component;

import unknow.kyhtanil.common.component.StatShared;
import unknow.kyhtanil.common.component.Position;
import unknow.kyhtanil.common.component.Sprite;
import unknow.kyhtanil.common.component.Velocity;
import unknow.kyhtanil.common.pojo.UUID;

public class Spawn extends Component {
	public UUID uuid = null;
	public Sprite sprite = null;
	public StatShared m;
	public Position p;
	public Velocity v;

	public Spawn() {
	}

	public Spawn(UUID uuid, Sprite sprite, StatShared m, Position p, Velocity v) {
		this.uuid = uuid;
		this.sprite = new Sprite(sprite);
		this.m = m;
		this.p = p;
		this.v = v;
	}

	@Override
	public String toString() {
		return "Spawn [uuid=" + uuid + ", sprite=" + sprite + ", m=" + m + ", p=" + p + ", v=" + v + "]";
	}
}