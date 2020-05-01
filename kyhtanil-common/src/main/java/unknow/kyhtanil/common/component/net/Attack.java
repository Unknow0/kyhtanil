package unknow.kyhtanil.common.component.net;

import com.artemis.Component;

import unknow.kyhtanil.common.pojo.UUID;

/**
 * Attack event
 * 
 * @author unknow
 */
public class Attack extends Component {
	/** uuid of attacker */
	public UUID uuid = null;
	/** id of the skill */
	public int id = 0;
	/** the target (UUID|Position) */
	public Object target = null;

	/**
	 * create new Attack
	 */
	public Attack() {
	}

	/**
	 * create new Attack
	 * 
	 * @param uuid
	 * @param id
	 * @param target
	 */
	public Attack(UUID uuid, int id, Object target) {
		this.uuid = uuid;
		this.id = id;
		this.target = target;
	}

	@Override
	public String toString() {
		return "Attack [uuid=" + uuid + ", id=" + id + ", target=" + target + "]";
	}
}