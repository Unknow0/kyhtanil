package unknow.kyhtanil.common.component;

import com.artemis.Component;

/**
 * a component to copy
 * 
 * @author unknow
 * @param <T> the Settable type
 */
public interface Setable<T extends Component> {
	/**
	 * set the value from another component
	 * 
	 * @param t
	 */
	public void set(T t);
}
