package unknow.kyhtanil.server.component;

import io.netty.channel.*;
import unknow.kyhtanil.server.pojo.*;

import com.artemis.*;

public class StateComp extends PooledComponent
	{
	public static enum States
		{
		NOT_LOGGED, LOGGED, IN_GAME;
		}

	public Account account;
	public Channel channel;
	public States state;

	protected void reset()
		{
		account=null;
		channel=null;
		state=null;
		}
	}