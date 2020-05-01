package unknow.kyhtanil.common.component;

import com.artemis.PooledComponent;

/**
 * a sprite
 * 
 * @author unknow
 */
public class Sprite extends PooledComponent {
	/** the texture for this sprite */
	public String tex;

	/** the width */
	public float w;
	/** the height */
	public float h;
	/** the rotation to apply */
	public float rotation = 0;

	/**
	 * create new Sprite
	 */
	public Sprite() {
	}

	/**
	 * create new Sprite
	 * 
	 * @param sprite
	 */
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

	/**
	 * @param sprite the values
	 */
	public void set(Sprite sprite) {
		this.tex = sprite.tex;
		this.w = sprite.w;
		this.h = sprite.h;
		this.rotation = sprite.rotation;
	}

	@Override
	public String toString() {
		return "Sprite [tex=" + tex + ", w=" + w + ", h=" + h + ", rotation=" + rotation + "]";
	}
}
