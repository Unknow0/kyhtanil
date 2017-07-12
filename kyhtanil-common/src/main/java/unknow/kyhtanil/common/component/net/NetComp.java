package unknow.kyhtanil.common.component.net;

import io.netty.channel.*;

import com.artemis.*;

public class NetComp extends PooledComponent {
	public Channel channel=null;

	public NetComp() {
	}

	public NetComp(Channel channel) {
		this.channel=channel;
	}

	public void reset() {
		channel=null;
	}

	public String toString() {
		return "channel: "+channel;
	}
}