package unknow.kyhtanil.server;

import io.netty.bootstrap.*;
import io.netty.buffer.*;
import io.netty.channel.*;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.nio.*;
import io.netty.channel.socket.*;
import io.netty.channel.socket.nio.*;
import io.netty.handler.codec.*;

import java.util.*;

import org.slf4j.*;

import unknow.common.tools.*;
import unknow.kyhtanil.common.component.net.*;
import unknow.kyhtanil.common.util.*;

import com.artemis.*;
import com.esotericsoftware.kryo.*;
import com.esotericsoftware.kryo.io.*;

public class Server
	{
	private static final Logger log=LoggerFactory.getLogger(Server.class);
	private static BaseComponentMapper<NetComp> net;
	private static Kryos kryos;

	public static class Decoder extends ByteToMessageDecoder
		{
		protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
			{
			if(in.readableBytes()<1)
				return;
			try
				{
				byte[] dst=new byte[in.readableBytes()];
				in.getBytes(in.readerIndex(), dst);

				Input input=new Input(dst);

				Object o=kryos.read(input);
				if(o instanceof Component)
					{
					Integer e=ArtemisInstantiator.lastCreated();
					NetComp netComp=net.get(e);
					netComp.channel=ctx.channel();
					log.trace("read: {} {} {}", e, o.getClass(), JsonUtils.toString(o, true));
					}
				else if(o instanceof byte[])
					{
					if(!Arrays.equals((byte[])o, kryos.hash()))
						ctx.close();
					}

				in.skipBytes(input.position());
				}
			catch (KryoException e)
				{
				if(!e.getMessage().contains("Buffer underflow"))
					{
					log.error(e.getMessage(), e);
					}
				}
			}
		}

	@Sharable
	public static class Encoder extends MessageToByteEncoder<Object>
		{
		protected void encode(ChannelHandlerContext ctx, Object data, ByteBuf out) throws Exception
			{
			log.trace("write: {}: {}", data.getClass(), JsonUtils.toString(data, true));

			try
				{
				ByteBufOutputStream buf=new ByteBufOutputStream(out);
				Output output=new Output(buf);
				kryos.write(output, data);
				output.close();
				}
			catch (Exception e)
				{
				log.error(e.getMessage(), e);
				}
			}
		}

	@Sharable
	public static class Handler extends ChannelHandlerAdapter
		{
		public void channelActive(ChannelHandlerContext ctx)
			{
			ctx.writeAndFlush(kryos.hash());
			}

		public void channelInactive(ChannelHandlerContext ctx) throws Exception
			{
			}

		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
			{
			}
		}

	private EventLoopGroup serverGroup=new NioEventLoopGroup();
	private ServerBootstrap server=new ServerBootstrap();
	private EventLoopGroup clientGroup=new NioEventLoopGroup();
	private Bootstrap client=new Bootstrap();
	private List<ChannelFuture> bind=new ArrayList<ChannelFuture>();

	private final Encoder encoder=new Encoder();
	private final Handler handler=new Handler();
	private final ChannelInitializer<SocketChannel> initializer=new ChannelInitializer<SocketChannel>()
		{
			@Override
			public void initChannel(SocketChannel ch) throws Exception
				{
				ch.pipeline().addLast(new Decoder(), encoder, handler);
				}
		};

	public Server(World world) throws Exception
		{
		net=BaseComponentMapper.getFor(NetComp.class, world);

		kryos=new Kryos(world, NetComp.class);

		server.group(serverGroup).channel(NioServerSocketChannel.class);
		server.childHandler(initializer);
		server.option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);

		client.group(clientGroup).channel(NioSocketChannel.class);
		client.handler(initializer);
		}

	public void bind(int port) throws InterruptedException
		{
		ChannelFuture b=server.bind(port).sync();
		bind.add(b.channel().closeFuture());
		}

	public void connect(String host, int port) throws InterruptedException
		{
		ChannelFuture connect=client.connect(host, port);
		connect.sync();
		}

	public void waitShutdown() throws InterruptedException
		{
		for(int i=0; i<bind.size(); i++)
			bind.get(i).sync();
		}

	public void close()
		{
		clientGroup.shutdownGracefully();
		serverGroup.shutdownGracefully();
		}
	}
