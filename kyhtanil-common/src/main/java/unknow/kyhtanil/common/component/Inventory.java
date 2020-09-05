/**
 * 
 */
package unknow.kyhtanil.common.component;

import java.util.ArrayList;
import java.util.List;

import com.artemis.PooledComponent;

import unknow.kyhtanil.common.pojo.Item;

/**
 * @author unknow
 */
public class Inventory extends PooledComponent {
	/** list of all item id */
	public List<Item> items = new ArrayList<>(10);

	@Override
	protected void reset() {
		items.clear();
	}

	/**
	 * @author unknow
	 */
	public static class Add extends ArrayList<Item> implements Setable<Inventory> {
		private static final long serialVersionUID = 1L;

		@Override
		public Class<Inventory> component() {
			return Inventory.class;
		}

		@Override
		public void setTo(Inventory t) {
			t.items.addAll(this);
		}
	}
}