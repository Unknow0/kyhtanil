package unknow.kyhtanil.client.graphics;

import sun.misc.*;
import unknow.common.*;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.*;
import com.kotcrab.vis.ui.widget.*;

public class DrawableList<T extends Widget> extends VisList<T> {
	/** hack to access private member */
	@SuppressWarnings("restriction")
	private Unsafe u;
	private long itemHeight;
	private long prefHeight;
	private long prefWidth;
	private long cullingArea;

	@SuppressWarnings("restriction")
	public DrawableList() throws NoSuchFieldException, SecurityException {
		Class<?> cl = this.getClass().getSuperclass().getSuperclass();
		u = Reflection.unsafe();
		itemHeight = u.objectFieldOffset(cl.getDeclaredField("itemHeight"));
		prefHeight = u.objectFieldOffset(cl.getDeclaredField("prefHeight"));
		prefWidth = u.objectFieldOffset(cl.getDeclaredField("prefWidth"));
		cullingArea = u.objectFieldOffset(cl.getDeclaredField("cullingArea"));
	}

	@SuppressWarnings("restriction")
	@Override
	public void layout() {
		if (u == null)
			return;
		float ih = 0;
		float pw = 0;
		float ph = 0;
		ListStyle style = getStyle();
		Drawable selection = style.selection;
		Drawable background = style.background;

		Array<T> items = getItems();
		for (int i = 0; i < items.size; i++) {
			T t = items.get(i);
			if (t.getPrefHeight() > ih)
				ih = t.getPrefHeight();
			if (t.getPrefWidth() > pw)
				pw = t.getPrefWidth();
		}
		ph = ih * items.size;
		pw += selection.getRightWidth() + selection.getLeftWidth();
		if (background != null) {
			pw += background.getLeftWidth() + background.getRightWidth();
			ph += background.getTopHeight() + background.getBottomHeight();
		}

		u.putFloat(this, itemHeight, ih);
		u.putFloat(this, prefHeight, ph);
		u.putFloat(this, prefWidth, pw);
	}

	@SuppressWarnings("restriction")
	@Override
	public void draw(Batch batch, float parentAlpha) {
		validate();

		ArraySelection<T> selection = getSelection();
		Array<T> items = getItems();
		ListStyle style = getStyle();
		Rectangle cullingArea = (Rectangle) u.getObject(this, this.cullingArea);
		BitmapFont font = style.font;
		Drawable selectedDrawable = style.selection;
		Color fontColorSelected = style.fontColorSelected;
		Color fontColorUnselected = style.fontColorUnselected;

		Color color = getColor();
		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

		float x = getX(), y = getY(), width = getWidth(), height = getHeight();
		float itemY = height;

		Drawable background = style.background;
		if (background != null) {
			background.draw(batch, x, y, width, height);
			float leftWidth = background.getLeftWidth();
			x += leftWidth;
			itemY -= background.getTopHeight();
			width -= leftWidth + background.getRightWidth();
		}

		font.setColor(fontColorUnselected.r, fontColorUnselected.g, fontColorUnselected.b, fontColorUnselected.a * parentAlpha);

		float itemHeight = getItemHeight();
		for (int i = 0; i < items.size; i++) {
			if (cullingArea == null || (itemY - itemHeight <= cullingArea.y + cullingArea.height && itemY >= cullingArea.y)) {
				T item = items.get(i);
				boolean selected = selection.contains(item);
				if (selected) {
					selectedDrawable.draw(batch, x, y + itemY - itemHeight, width, itemHeight);
					font.setColor(fontColorSelected.r, fontColorSelected.g, fontColorSelected.b, fontColorSelected.a * parentAlpha);
				}
				item.setX(x);
				item.setY(y);
				item.setHeight(itemHeight);
				item.setWidth(width);
				item.draw(batch, parentAlpha);
				if (selected) {
					font.setColor(fontColorUnselected.r, fontColorUnselected.g, fontColorUnselected.b, fontColorUnselected.a * parentAlpha);
				}
				y += itemHeight;
			} else if (itemY < cullingArea.y) {
				break;
			}
			itemY -= itemHeight;
		}
	}

	@Override
	public Actor hit(float x, float y, boolean touchable) {
		if (touchable && this.getTouchable() != Touchable.enabled)
			return null;
		if (x < 0 || x > getWidth() || y < 0 || y > getHeight())
			return null;
		float itemHeight = getItemHeight();
		int i = (int) (y / itemHeight);
		return getItems().get(i).hit(x, y - i * itemHeight, touchable);
	}
}
