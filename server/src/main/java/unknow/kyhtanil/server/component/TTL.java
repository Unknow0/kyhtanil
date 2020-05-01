/**
 * 
 */
package unknow.kyhtanil.server.component;

import com.artemis.Component;
import com.artemis.annotations.PooledWeaver;

/**
 * entity with a ttl
 * 
 * @author unknow
 */
@PooledWeaver
public class TTL extends Component {
	/** remaining ttl */
	public float ttl;

	@Override
	public String toString() {
		return "TTL [ttl=" + ttl + "]";
	}
}
