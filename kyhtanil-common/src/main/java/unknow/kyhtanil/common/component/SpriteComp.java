package unknow.kyhtanil.common.component;

import com.artemis.PooledComponent;

public class SpriteComp extends PooledComponent {
	public String tex;

	public float w, h;
	public float rotation = 0;

	public SpriteComp() {
	}

	public SpriteComp(SpriteComp sprite) {
		this.tex = sprite.tex;
		this.w = sprite.w;
		this.h = sprite.h;
		this.rotation = sprite.rotation;
	}

	protected void reset() {
		tex = null;
		h = rotation = w = 0;
	}

	public void set(SpriteComp sprite) {
		this.tex = sprite.tex;
		this.w = sprite.w;
		this.h = sprite.h;
		this.rotation = sprite.rotation;
	}
}
