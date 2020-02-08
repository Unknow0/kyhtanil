package unknow.kyhtanil.server.component;

import java.util.*;

import com.artemis.*;

public class CompositeComponent<T> extends PooledComponent {
	public List<T> list = new ArrayList<T>();

	public void add(T t) {
		list.add(t);
	}

	protected void reset() {
		list.clear();
	}
}
