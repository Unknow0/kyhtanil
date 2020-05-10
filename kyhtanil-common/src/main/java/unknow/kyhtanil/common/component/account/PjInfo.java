package unknow.kyhtanil.common.component.account;

import com.artemis.Component;

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
	 */
	public PjInfo(Position p, StatShared stats, StatBase perso) {
		this.p = p;
		this.stats = stats;
		this.perso = perso;
	}

	@Override
	public String toString() {
		return "PjInfo [p=" + p + ",  stats=" + stats + ", perso=" + perso + "]";
	}
}