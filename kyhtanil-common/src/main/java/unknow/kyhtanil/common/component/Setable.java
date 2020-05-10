package unknow.kyhtanil.common.component;

import com.artemis.Component;
import com.artemis.PooledComponent;

/**
 * a component to copy
 * 
 * @author unknow
 * @param <T> the Setable type
 */
public abstract class Setable<T extends Component> extends PooledComponent {
	/**
	 * set the value from another component
	 * 
	 * @param t
	 */
	public abstract void set(T t);
}
