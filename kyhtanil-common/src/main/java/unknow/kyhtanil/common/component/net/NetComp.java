package unknow.kyhtanil.common.component.net;

import io.netty.channel.*;

import com.artemis.*;

public class NetComp extends PooledComponent {
	public Channel channel = null;

	public NetComp() {
	}

	public NetComp(Channel channel) {
		this.channel = channel;
	}

	@Override
	public void reset() {
		channel = null;
	}

	@Override
	public String toString() {
		return "NetComp [channel=" + channel + "]";
	}
}