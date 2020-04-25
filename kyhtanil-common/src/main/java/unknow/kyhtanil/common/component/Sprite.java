package unknow.kyhtanil.common.component;

import com.artemis.PooledComponent;

public class Sprite extends PooledComponent {
	public String tex;

	public float w, h;
	public float rotation = 0;

	public Sprite() {
	}

	public Sprite(Sprite sprite) {
		this.tex = sprite.tex;
		this.w = sprite.w;
		this.h = sprite.h;
		this.rotation = sprite.rotation;
	}

	@Override
	protected void reset() {
		tex = null;
		h = rotation = w = 0;
	}

	public void set(Sprite sprite) {
		this.tex = sprite.tex;
		this.w = sprite.w;
		this.h = sprite.h;
		this.rotation = sprite.rotation;
	}

	@Override
	public String toString() {
		return "SpriteComp [tex=" + tex + ", w=" + w + ", h=" + h + ", rotation=" + rotation + "]";
	}
}
