package unknow.kyhtanil.client.graphics;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisTable;

import unknow.kyhtanil.client.system.State;
import unknow.kyhtanil.common.pojo.CharDesc;

/**
 * the char list
 * 
 * @author unknow
 */
public class CharList extends VisTable {
	private CharDesc[] cache;

	private CharActor selected;

	/**
	 * create new CharList
	 */
	public CharList() {
		addCaptureListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Actor hit = hit(x, y, true);
				if (hit != null) {
					if (selected != null)
						selected.setSelected(false);
					selected = (CharActor) hit;
					selected.setSelected(true);
				}
			}
		});
	}

	@Override
	public void validate() {
		if (State.state.chars != cache) {
			selected = null;
			clearChildren();
			cache = State.state.chars;
			for (CharDesc c : cache) {
				CharActor charActor = new CharActor(c);
				if (selected == null) {
					selected = charActor;
					selected.setSelected(true);
				}
				add(charActor).row();
			}
		}
		super.validate();
	}

	/**
	 * @return the selected char
	 */
	public CharDesc selected() {
		return selected.c;
	}
}
