package unknow.kyhtanil.common.component.net;

import unknow.kyhtanil.common.pojo.*;

import com.artemis.*;

public class DamageReport extends Component {
	public UUID uuid=null;
	public int damage=0;

	public DamageReport() {
	}

	public DamageReport(UUID uuid, int damage) {
		this.uuid=uuid;
		this.damage=damage;
	}

	public String toString() {
		return "uuid: "+uuid+", damage: "+damage;
	}
}