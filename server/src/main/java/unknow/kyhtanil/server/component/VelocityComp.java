package unknow.kyhtanil.server.component;

import com.artemis.*;

public class VelocityComp extends PooledComponent
	{
	public float dirX;
	public float dirY;
	public float speed;

	protected void reset()
		{
		dirX=dirY=speed=0;
		}
	}
