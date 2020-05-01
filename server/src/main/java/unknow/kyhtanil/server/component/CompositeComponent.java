package unknow.kyhtanil.server.component;

import java.util.ArrayList;
import java.util.List;

import com.artemis.PooledComponent;

/**
 * a list component
 * 
 * @author unknow
 * @param <T> type of subcomponent
 */
public class CompositeComponent<T> extends PooledComponent {
	/** list all component */
	public List<T> list = new ArrayList<>();

	/**
	 * add a component to the list
	 * 
	 * @param t object to add
	 */
	public void add(T t) {
		list.add(t);
	}

	@Override
	protected void reset() {
		list.clear();
	}
}
