package unknow.kyhtanil.server.component;

import java.util.IdentityHashMap;
import java.util.Map;

import com.artemis.Component;
import com.artemis.PooledComponent;

import unknow.kyhtanil.common.component.Setable;

/**
 * contains all component that have been updated and should be reported to client
 * 
 * @author unknow
 */
public class Dirty extends PooledComponent {
	/** changed component */
	public Map<Class<?>, Setable<?>> map = new IdentityHashMap<>();

	@Override
	public void reset() {
		map.clear();
	}

	/**
	 * add a dirty component
	 * 
	 * @param c component taht changed
	 */
	public void add(Setable<?> c) {
		map.put(c.getClass(), c);
	}

	/**
	 * get all changed component
	 * 
	 * @param exclude component to exclude
	 * @return the array of changed component
	 */
	public Component[] changed(Class<?> exclude) {
		int i = map.size();
		if (map.containsKey(exclude))
			i--;
		Component[] t = new Component[i];
		i = 0;
		for (Setable<?> c : map.values()) {
			if (exclude != c.getClass())
				t[i++] = (Component) c;
		}
		return t;
	}
}
