package unknow.kyhtanil.server.component;

import com.artemis.Component;
import com.artemis.annotations.EntityId;
import com.artemis.annotations.PooledWeaver;

/**
 * damage effect
 * 
 * @author unknow
 */
@PooledWeaver
public class Damage extends Component {
	public int base;
	public int slashing;
	public int blunt;
	public int piercing;
	public int lightning;
	public int fire;
	public int ice;

	/** source of the damage */
	@EntityId
	public int source;
	/** target of the damage */
	@EntityId
	public int target;

	@Override
	public String toString() {
		return "Damage [base=" + base + ", slashing=" + slashing + ", blunt=" + blunt + ", piercing=" + piercing + ", lightning=" + lightning + ", fire=" + fire + ", ice=" + ice + ", source=" + source + ", target=" + target + "]";
	}
}
