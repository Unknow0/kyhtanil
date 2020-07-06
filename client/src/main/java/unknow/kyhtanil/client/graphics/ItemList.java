/**
 * 
 */
package unknow.kyhtanil.client.graphics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import com.kotcrab.vis.ui.widget.VisLabel;

import unknow.common.data.IntIterator;
import unknow.kyhtanil.client.i18n.I18N;
import unknow.kyhtanil.client.system.State;
import unknow.kyhtanil.common.component.Inventory;

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
		IntIterator it = inventory.items.iterator();
		int c = 1;
		int i = it.nextInt();
		while (it.hasNext()) {
			int n = it.nextInt();
			if (n != i) {
				list.add(new VisLabel(I18N.get("item_name_" + i) + " (" + c + ")"));
				c = 0;
				i = n;
			}
			c++;
		}
		list.add(new VisLabel(I18N.get("item_name_" + i) + " (" + c + ")"));
		return list.iterator();
	}
}
