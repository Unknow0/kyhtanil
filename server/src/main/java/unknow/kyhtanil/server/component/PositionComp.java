package unknow.kyhtanil.server.component;

import com.artemis.*;

public class PositionComp extends PooledComponent
	{
	public float x, y;

	protected void reset()
		{
		x=y=0;
		}

	public double distance(PositionComp p)
		{
		return distance(p.x, p.y);
		}

	public double distance(float x, float y)
		{
		float dx=this.x-x;
		float dy=this.y-y;
		return Math.sqrt(dx*dx+dy*dy);
		}
	}
