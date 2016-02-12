package unknow.kyhtanil.server.component;

import io.netty.channel.*;
import unknow.kyhtanil.server.pojo.*;

import com.artemis.*;

public class StateComp extends PooledComponent
	{
	public Account account;
	public Channel channel;

	protected void reset()
		{
		account=null;
		channel=null;
		}
	}