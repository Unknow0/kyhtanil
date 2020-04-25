package unknow.kyhtanil.server.component;

import java.util.ArrayList;
import java.util.List;

import com.artemis.PooledComponent;

public class CompositeComponent<T> extends PooledComponent {
	public List<T> list = new ArrayList<>();

	public void add(T t) {
		list.add(t);
	}

	@Override
	protected void reset() {
		list.clear();
	}
}
