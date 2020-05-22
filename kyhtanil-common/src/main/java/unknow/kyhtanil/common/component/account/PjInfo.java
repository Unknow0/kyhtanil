package unknow.kyhtanil.common.component.account;

import com.artemis.Component;

import unknow.kyhtanil.common.component.Inventory;
import unknow.kyhtanil.common.component.Position;
import unknow.kyhtanil.common.component.StatBase;
import unknow.kyhtanil.common.component.StatShared;

/**
 * @author unknow
 */
public class PjInfo extends Component {
	/** the position of the char */
	public Position p;
	/** the shared stats */
	public StatShared stats = null;
	/** the base stats */
	public StatBase perso = null;
	/** start inventory */
	public Inventory.Add inventory = null;

	/**
	 * create new PjInfo
	 */
	public PjInfo() {
	}

	/**
	 * create new PjInfo
	 * 
	 * @param p
	 * @param stats
	 * @param perso
	 * @param inventory
	 */
	public PjInfo(Position p, StatShared stats, StatBase perso, Inventory.Add inventory) {
		this.p = p;
		this.stats = stats;
		this.perso = perso;
		this.inventory = inventory;
	}

	@Override
	public String toString() {
		return "PjInfo [p=" + p + ", stats=" + stats + ", perso=" + perso + ", inventory=" + inventory + "]";
	}
}