package unknow.kyhtanil.common.component;

import com.artemis.Component;

public interface Setable<T extends Component> {
	public void set(T t);
}
