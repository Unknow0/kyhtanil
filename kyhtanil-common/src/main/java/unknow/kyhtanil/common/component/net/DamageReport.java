package unknow.kyhtanil.common.component.net;

import unknow.kyhtanil.common.pojo.*;

import com.artemis.*;

/**
 * Damage report event
 * 
 * @author unknow
 */
public class DamageReport extends Component {
	/** uuid of the entity receiving damage */
	public UUID uuid = null;
	/** the damage */
	public int damage = 0;

	/**
	 * create new DamageReport
	 */
	public DamageReport() {
	}

	/**
	 * create new DamageReport
	 * 
	 * @param uuid
	 * @param damage
	 */
	public DamageReport(UUID uuid, int damage) {
		this.uuid = uuid;
		this.damage = damage;
	}

	@Override
	public String toString() {
		return "DamageReport [uuid=" + uuid + ", damage=" + damage + "]";
	}
}