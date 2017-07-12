package unknow.kyhtanil.server.component;

import unknow.kyhtanil.server.utils.*;

import com.artemis.*;

public class Projectile extends PooledComponent
	{
	public int source;
	public Event onHit;
	public String tex;

	@Override
	protected void reset()
		{
		source=-1;
		onHit=null;
		tex=null;
		}
	}
