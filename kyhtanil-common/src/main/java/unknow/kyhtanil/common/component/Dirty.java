package unknow.kyhtanil.common.component;

import java.util.IdentityHashMap;
import java.util.Map;

import com.artemis.Component;
import com.artemis.PooledComponent;

import unknow.kyhtanil.common.component.net.UpdateInfo;
import unknow.kyhtanil.common.pojo.UUID;

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
	 * get the UpdateInfo
	 * 
	 * @param uuid    the uuid of this entity
	 * @param exclude component to exclude
	 * @return the UpdateInfo of changed component (or null if no component changed)
	 */
	public UpdateInfo changed(UUID uuid, Class<?>... exclude) {
		int len = map.size();
		int elen = exclude.length;
		for (int i = 0; i < elen; i++) {
			if (map.containsKey(exclude[i]))
				len--;
		}
		if (len == 0)
			return null;
		Component[] t = new Component[len];
		len = 0;
		loop: for (Setable<?> c : map.values()) {
			for (int i = 0; i < elen; i++) {
				if (exclude[i] == c.getClass())
					continue loop;
			}
			t[len++] = c;
		}
		return new UpdateInfo(uuid, t);
	}
}