package unknow.kyhtanil.client.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import unknow.kyhtanil.client.system.State;
import unknow.kyhtanil.common.TexManager;
import unknow.kyhtanil.common.TexManager.Drawable;
import unknow.kyhtanil.common.component.StatShared;

/**
 * Actor taht draw a bar
 * 
 * @author unknow
 */
public abstract class BarActor extends Actor {
	private static final Drawable nine = TexManager.get("hud/bar");
	private final Texture back;
	private static final Texture black;
	static {
		Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
		pixmap.setColor(Color.BLACK);
		pixmap.fill();
		black = new Texture(pixmap);
	}

	/**
	 * create new BarActor
	 * 
	 * @param color RGBA8888 color
	 */
	public BarActor(int color) {
		Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
		pixmap.setColor(color);
		pixmap.fill();
		back = new Texture(pixmap);
	}

	/**
	 * @return the fill %
	 */
	protected abstract float rate();

	@Override
	public void draw(Batch batch, float parentAlpha) {
		float r = rate() * getWidth();
		batch.draw(black, getX(), getY(), getWidth(), getHeight());
		batch.draw(back, getX(), getY(), r, getHeight());
		nine.draw(batch, getX(), getY(), r, getHeight());
	}

	/**
	 * The char health bar
	 * 
	 * @author unknow
	 */
	public static class Hp extends BarActor {
		/**
		 * create new Hp
		 */
		public Hp() {
			super(0xff0000ff);
		}

		@Override
		protected float rate() {
			StatShared shared = State.state.shared();
			return (float) (shared.hp * 1. / shared.maxHp);
		}
	}

	/**
	 * the char mana bar
	 * 
	 * @author unknow
	 */
	public static class Mp extends BarActor {
		/**
		 * create new Mp
		 */
		public Mp() {
			super(0x0000ffff);
		}

		@Override
		protected float rate() {
			StatShared shared = State.state.shared();
			return (float) (shared.mp * 1. / shared.maxMp);
		}
	}
}
