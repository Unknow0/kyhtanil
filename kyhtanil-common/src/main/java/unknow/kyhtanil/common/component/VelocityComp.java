package unknow.kyhtanil.common.component;

import com.artemis.*;

public class VelocityComp extends PooledComponent
	{
	public float direction;
	public float speed;

	public VelocityComp()
		{
		}

	public VelocityComp(float direction, float speed)
		{
		this.direction=direction;
		this.speed=speed;
		}

	protected void reset()
		{
		}

	public void set(VelocityComp v)
		{
		this.direction=v.direction;
		this.speed=v.speed;
		}
	}
