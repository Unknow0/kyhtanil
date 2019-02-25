package unknow.kyhtanil.server.component;

import com.artemis.PooledComponent;

import unknow.kyhtanil.common.pojo.UUID;
import unknow.kyhtanil.server.utils.Event;

public class Projectile extends PooledComponent
	{
	public UUID source;
	public Event onHit;

	@Override
	protected void reset()
		{
		source=null;
		onHit=null;
		}
	}
