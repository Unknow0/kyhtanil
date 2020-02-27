package unknow.kyhtanil.client.graphics;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import unknow.kyhtanil.client.system.State;
import unknow.kyhtanil.client.system.TexManager;
import unknow.kyhtanil.client.system.TexManager.NineDrawable;
import unknow.kyhtanil.common.component.StatShared;

public abstract class BarActor extends Actor {
	private static final NineDrawable nine = (NineDrawable) TexManager.get("hud/bar");
	private final Texture back;

	public BarActor(int color) {
		Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
		pixmap.setColor(color);
		pixmap.fill();
		back = new Texture(pixmap);
	}

	protected abstract float rate();

	@Override
	public void draw(Batch batch, float parentAlpha) {
		float r = rate();

		batch.draw(back, getX(), getY(), getWidth(), getHeight());
		nine.setLeftWidth(getWidth() * r - nine.getMiddleWidth() / 2);
		nine.setRightWidth(getWidth() - nine.getLeftWidth() - nine.getMiddleWidth() / 2);
		nine.draw(batch, getX(), getY(), getWidth(), getHeight());
	}

	public static class Hp extends BarActor {
		public Hp() {
			super(0xff0000ff);
		}

		protected float rate() {
			StatShared shared = State.state.shared();
			return (float) (shared.hp * 1. / shared.maxHp);
		}
	}

	public static class Mp extends BarActor {
		public Mp() {
			super(0x0000ffff);
		}

		protected float rate() {
			StatShared shared = State.state.shared();
			return (float) (shared.mp * 1. / shared.maxMp);
		}
	}
}
