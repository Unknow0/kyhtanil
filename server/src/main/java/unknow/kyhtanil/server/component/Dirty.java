package unknow.kyhtanil.server.component;

import com.artemis.PooledComponent;

public class Dirty extends PooledComponent
	{
	public boolean dirty;

	@Override
	protected void reset()
		{
		dirty=false;
		}
	}
