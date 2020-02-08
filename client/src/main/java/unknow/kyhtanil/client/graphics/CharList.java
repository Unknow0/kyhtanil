package unknow.kyhtanil.client.graphics;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisTable;

import unknow.kyhtanil.client.system.State;
import unknow.kyhtanil.common.pojo.CharDesc;

public class CharList extends VisTable {
	private CharDesc[] cache;

	private CharActor selected;

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
			clearChildren();
			cache = State.state.chars;
			for (int i = 0; i < 3; i++)
				for (CharDesc c : cache)
					add(new CharActor(c)).row();
		}
		super.validate();
	}

	public CharDesc selected() {
		return selected.c;
	}
}
