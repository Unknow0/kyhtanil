package unknow.kyhtanil.common.component.net;

import com.artemis.Component;

import unknow.kyhtanil.common.pojo.UUID;

/**
 * Move event
 * 
 * @author unknow
 */
public class Move extends Component {
	/** uuid of the moving entity */
	public UUID uuid = null;
	/** new x */
	public float x = 0;
	/** new y */
	public float y = 0;
	/** new direction */
	public float direction = 0;

	/**
	 * create new Move
	 */
	public Move() {
	}

	/**
	 * create new Move
	 * 
	 * @param uuid
	 * @param x
	 * @param y
	 * @param direction
	 */
	public Move(UUID uuid, float x, float y, float direction) {
		this.uuid = uuid;
		this.x = x;
		this.y = y;
		this.direction = direction;
	}

	@Override
	public String toString() {
		return "Move [uuid=" + uuid + ", x=" + x + ", y=" + y + ", direction=" + direction + "]";
	}
}