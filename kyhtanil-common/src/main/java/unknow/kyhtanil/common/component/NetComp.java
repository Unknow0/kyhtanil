package unknow.kyhtanil.common.component;

import com.artemis.*;

import io.netty.channel.*;

public class NetComp extends PooledComponent
	{
	public Channel channel;

	protected void reset()
		{
		channel=null;
		}
	}
