package unknow.kyhtanil.server.component;

import com.artemis.PooledComponent;

import io.netty.channel.Channel;

/**
 * player state
 * 
 * @author unknow
 */
public class StateComp extends PooledComponent {
	/** all player states */
	public static enum States {
		LOGGED, IN_GAME;
	}

	/** account id */
	public int account;
	/** the channel to the client */
	public Channel channel;
	/** the actual state */
	public States state;

	@Override
	protected void reset() {
		account = 0;
		channel = null;
		state = null;
	}

	/**
	 * copy state
	 * 
	 * @param s value to copy
	 */
	public void set(StateComp s) {
		account = s.account;
		channel = s.channel;
		state = s.state;
	}
}