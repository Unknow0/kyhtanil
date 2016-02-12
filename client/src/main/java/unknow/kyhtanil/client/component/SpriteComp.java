package unknow.kyhtanil.client.component;

import com.artemis.*;
import com.badlogic.gdx.graphics.g2d.*;

public class SpriteComp extends PooledComponent
	{
	public TextureRegion tex;

	public float w, h;
	public float rotation=0;

	protected void reset()
		{
		tex=null;
		h=rotation=w=0;
		}
	}
