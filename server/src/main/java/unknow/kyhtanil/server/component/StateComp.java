package unknow.kyhtanil.server.component;

import com.artemis.PooledComponent;

import io.netty.channel.Channel;

public class StateComp extends PooledComponent {
	public static enum States {
		NOT_LOGGED, LOGGED, IN_GAME;
	}

	public int account;
	public Channel channel;
	public States state;

	@Override
	protected void reset() {
		account = 0;
		channel = null;
		state = null;
	}

	public void set(StateComp s) {
		account = s.account;
		channel = s.channel;
		state = s.state;
	}
}