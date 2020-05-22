/**
 * 
 */
package unknow.kyhtanil.common.component;

import com.artemis.PooledComponent;

import unknow.common.data.IntArrayList;

/**
 * @author unknow
 */
public class Inventory extends PooledComponent {
	/** list of all item id */
	public IntArrayList items = new IntArrayList(10);

	@Override
	protected void reset() {
		items.clear();
	}

	/**
	 * @author unknow
	 */
	public static class Add extends IntArrayList implements Setable<Inventory> {
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