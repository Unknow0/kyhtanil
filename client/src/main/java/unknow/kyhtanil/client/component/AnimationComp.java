package unknow.kyhtanil.client.component;

import com.artemis.*;
import com.badlogic.gdx.graphics.g2d.*;

public class AnimationComp extends PooledComponent {
	public Animation animation;
	public float stateTime;
	public float frameDuration;
	public int playMode;

	public TextureRegion get() {
		return animation.getKeyFrame(stateTime);
	}

	protected void reset() {
		animation = null;
		stateTime = frameDuration = 0;
	}
}
