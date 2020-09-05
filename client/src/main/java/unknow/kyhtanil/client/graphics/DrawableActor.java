/**
 * 
 */
package unknow.kyhtanil.client.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import unknow.kyhtanil.common.TexManager;
import unknow.kyhtanil.common.TexManager.Drawable;

/**
 * @author unknow
 */
public class DrawableActor extends Actor {
	private Drawable d;

	public DrawableActor() {
	}

	public void setTexture(String t) {
		d = TexManager.get(t);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		if (d != null)
			d.draw(batch, getX(), getY(), getWidth(), getHeight());
	}
}
