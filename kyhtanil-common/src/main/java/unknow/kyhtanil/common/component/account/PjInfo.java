package unknow.kyhtanil.common.component.account;

import com.artemis.Component;

import unknow.kyhtanil.common.component.StatBase;
import unknow.kyhtanil.common.component.StatShared;

public class PjInfo extends Component {
	public float x = 0;
	public float y = 0;
	public StatShared stats = null;
	public StatBase perso = null;

	public PjInfo() {
	}

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