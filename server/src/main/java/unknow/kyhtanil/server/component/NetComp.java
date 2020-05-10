package unknow.kyhtanil.server.component;

import com.artemis.PooledComponent;

import io.netty.channel.Channel;

/**
 * source of network event
 * 
 * @author unknow
 */
public class NetComp extends PooledComponent {
	/** the client */
	public Channel channel = null;

	@Override
	public void reset() {
		channel = null;
	}

	@Override
	public String toString() {
		return "NetComp [channel=" + channel + "]";
	}
}