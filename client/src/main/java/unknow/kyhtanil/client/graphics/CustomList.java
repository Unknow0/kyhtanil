package unknow.kyhtanil.client.graphics;

import java.util.Iterator;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTable;

/**
 * the char list
 * 
 * @author unknow
 * @param <T> type of actor
 */
public abstract class CustomList<T extends Actor> extends VisTable {
	private final Drawable border = VisUI.getSkin().getDrawable("border");

	protected T selection;

	/**
	 * create new CustomList
	 */
	public CustomList() {
		addCaptureListener(new ClickListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Actor hit = hit(x, y, true);
				if (hit != null) {
					selection = (T) hit;
				}
			}
		});
		validate();
	}

	protected abstract Iterator<T> content();

	@Override
	public void layout() {
		clearChildren();
		Iterator<T> it = content();
		while (it.hasNext()) {
			row();
			add(it.next());
		}
		super.layout();
	}

	@Override
	protected void drawBackground(Batch batch, float parentAlpha, float x, float y) {
		super.drawBackground(batch, parentAlpha, x, y);
		if (selection != null)
			border.draw(batch, selection.getX()+x, selection.getY()+y, selection.getWidth(), selection.getHeight());
	}
}
