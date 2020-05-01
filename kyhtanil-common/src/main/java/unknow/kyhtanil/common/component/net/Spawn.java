package unknow.kyhtanil.common.component.net;

import com.artemis.Component;

import unknow.kyhtanil.common.component.Position;
import unknow.kyhtanil.common.component.Sprite;
import unknow.kyhtanil.common.component.StatShared;
import unknow.kyhtanil.common.component.Velocity;
import unknow.kyhtanil.common.pojo.UUID;

/**
 * spawn event
 * 
 * @author unknow
 */
public class Spawn extends Component {
	/** uuid of the new identity */
	public UUID uuid = null;
	/** sprite for the new entity */
	public Sprite sprite = null;
	/** the stat */
	public StatShared m;
	/** the position */
	public Position p;
	/** the velocity */
	public Velocity v;

	/**
	 * create new Spawn
	 */
	public Spawn() {
	}

	/**
	 * create new Spawn
	 * @param uuid
	 * @param sprite
	 * @param m
	 * @param p
	 * @param v
	 */
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