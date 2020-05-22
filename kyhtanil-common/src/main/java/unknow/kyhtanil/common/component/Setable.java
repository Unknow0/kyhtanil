package unknow.kyhtanil.common.component;

import com.artemis.Component;

/**
 * a component to copy
 * 
 * @author unknow
 * @param <T> the Setable type
 */
public interface Setable<T extends Component> {

	/**
	 * the component to set
	 * 
	 * @return the settable class
	 */
	Class<T> component();

	/**
	 * set the value to another component
	 * 
	 * @param t the component to set value
	 */
	void setTo(T t);
}
