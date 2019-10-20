package unknow.kyhtanil.common.component;

import com.artemis.*;

public class VelocityComp extends PooledComponent implements Setable<VelocityComp>
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

	@Override
	protected void reset()
		{
		}

	@Override
	public void set(VelocityComp v)
		{
		this.direction=v.direction;
		this.speed=v.speed;
		}
	}
