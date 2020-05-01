package unknow.kyhtanil.common.component.account;

import com.artemis.Component;

import unknow.kyhtanil.common.component.StatBase;
import unknow.kyhtanil.common.component.StatShared;

/**
 * @author unknow
 */
public class PjInfo extends Component {
	/** the x position of the char */
	public float x = 0;
	/** the x position of the char */
	public float y = 0;
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
	 * @param x
	 * @param y
	 * @param stats
	 * @param perso
	 */
	public PjInfo(float x, float y, StatShared stats, StatBase perso) {
		this.x = x;
		this.y = y;
		this.stats = stats;
		this.perso = perso;
	}

	@Override
	public String toString() {
		return "PjInfo [x=" + x + ", y=" + y + ", stats=" + stats + ", perso=" + perso + "]";
	}
}