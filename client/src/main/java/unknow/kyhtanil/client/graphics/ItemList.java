/**
 * 
 */
package unknow.kyhtanil.client.graphics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import com.kotcrab.vis.ui.widget.VisLabel;

import unknow.kyhtanil.client.i18n.I18N;
import unknow.kyhtanil.client.system.State;
import unknow.kyhtanil.common.component.Inventory;
import unknow.kyhtanil.common.pojo.Item;

/**
 * @author unknow
 */
public class ItemList extends CustomList<VisLabel> {
	private static final Inventory inventory = State.state.inventory();

	@Override
	protected Iterator<VisLabel> content() {
		if (inventory.items.isEmpty())
			return Collections.emptyIterator();
		ArrayList<VisLabel> list = new ArrayList<>();
		Iterator<Item> it = inventory.items.iterator();
//		int c = 1;
//		Item i = it.next();
		while (it.hasNext()) {
			Item n = it.next();
//			if (n.getBase() != i.getBase()) {
			list.add(new VisLabel(I18N.get("item_name_" + n.getBase())));
			// TODO add popup
//				c = 0;
//				i = n;
//			}
//			c++;
		}
//		list.add(new VisLabel(I18N.get("item_name_" + i.getBase()) + " (" + c + ")"));
		return list.iterator();
	}
}
