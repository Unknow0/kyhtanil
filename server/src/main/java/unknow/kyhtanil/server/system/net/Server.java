package unknow.kyhtanil.server.system.net;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artemis.BaseSystem;
import com.artemis.Component;
import com.artemis.EntityEdit;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import unknow.kyhtanil.common.util.KyhtanilSerialize;
import unknow.kyhtanil.server.component.NetComp;

/**
 * The server move event from network to world
 * 
 * @author unknow
 */
public class Server extends BaseSystem {
	private static final Logger log = LoggerFactory.getLogger(Server.class);

	private EventLoopGroup serverGroup = new NioEventLoopGroup();
	private ServerBootstrap server = new ServerBootstrap();
	private EventLoopGroup clientGroup = new NioEventLoopGroup();
	private Bootstrap client = new Bootstrap();
	private List<ChannelFuture> bind = new ArrayList<>();

	private final Encoder encoder = new Encoder();
	private final Handler handler = new Handler();

	private List<E> list = new ArrayList<>();
	private List<E> back = new ArrayList<>();

	/**
	 * create new Server
	 * 
	 * @param port the port to listen to
	 * @throws InterruptedException
	 */
	public Server(int port) throws InterruptedException {
		ChannelInitializer<SocketChannel> initializer = new ChannelInitializer<SocketChannel>() {
			@Override
			public void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addLast(new Decoder(), encoder, handler);
			}
		};

		server.group(serverGroup).channel(NioServerSocketChannel.class);
		server.childHandler(initializer);
		server.option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);

		ChannelFuture b = server.bind(port).sync();
		bind.add(b.channel().closeFuture());

		client.group(clientGroup).channel(NioSocketChannel.class);
		client.handler(initializer);
	}

	@Override
	protected void processSystem() {
		synchronized (this) {
			List<E> o = list;
			list = back;
			back = o;
		}

		for (E e : back) {
			int create = world.create();
			EntityEdit edit = world.edit(create);
			edit.add(e.o);
			edit.create(NetComp.class).channel = e.c;
		}
		back.clear();
	}

	/**
	 * connect to somewhere
	 * 
	 * @param host the host
	 * @param port the port
	 * @throws InterruptedException
	 */
	public void connect(String host, int port) throws InterruptedException {
		ChannelFuture connect = client.connect(host, port);
		connect.sync();
	}

	/**
	 * whait until the server closed
	 * 
	 * @throws InterruptedException
	 */
	public void waitShutdown() throws InterruptedException {
		for (int i = 0; i < bind.size(); i++)
			bind.get(i).sync();
	}

	/**
	 * shutdown the server
	 */
	public void close() {
		clientGroup.shutdownGracefully();
		serverGroup.shutdownGracefully();
	}

	protected class E {
		Component o;
		Channel c;

		public E(Component o, Channel c) {
			this.o = o;
			this.c = c;
		}
	}

	private class Decoder extends ByteToMessageDecoder {
		@Override
		protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
			if (buf.readableBytes() < 1)
				return;
			buf.markReaderIndex();
			try {
				Object o = KyhtanilSerialize.read(new ByteBufInputStream(buf));
				log.trace("read: {} {}", o.getClass(), o);
				if (o instanceof Component) {
					list.add(new E((Component) o, ctx.channel()));
				} else if (o instanceof byte[]) {
					if (!Arrays.equals((byte[]) o, KyhtanilSerialize.hash()))
						ctx.close();
				}
			} catch (IOException e) {
				buf.resetReaderIndex();
				if (!"end of stream reached".equals(e.getMessage()))
					log.error("decoder error", e);
			}
		}
	}

	@Sharable
	private class Encoder extends MessageToByteEncoder<Object> {
		@Override
		protected void encode(ChannelHandlerContext ctx, Object data, ByteBuf buf) throws Exception {
			try {
				log.trace("write: {}: {}", data.getClass(), data);
				ByteBufOutputStream out = new ByteBufOutputStream(buf);
				KyhtanilSerialize.write(data, out);
			} catch (Throwable e) {
				log.error(e.getMessage(), e);
				ctx.close();
			}
		}
	}

	@Sharable
	private class Handler extends ChannelHandlerAdapter {
		@Override
		public void channelActive(ChannelHandlerContext ctx) {
			ctx.writeAndFlush(KyhtanilSerialize.hash());
		}

		@Override
		public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		}

		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		}
	}
}
