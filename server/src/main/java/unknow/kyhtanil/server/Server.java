package unknow.kyhtanil.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artemis.BaseComponentMapper;
import com.artemis.Component;
import com.artemis.World;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
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
import unknow.common.tools.JsonUtils;
import unknow.kyhtanil.common.component.net.NetComp;
import unknow.kyhtanil.common.util.ArtemisInstantiator;
import unknow.kyhtanil.common.util.Kryos;

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
				log.trace("read: {} {}", o.getClass(), JsonUtils.toString(o, true));
				if(o instanceof Component)
					{
					Integer e=ArtemisInstantiator.lastCreated();
					NetComp netComp=net.get(e);
					netComp.channel=ctx.channel();
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
		@Override
		public void channelActive(ChannelHandlerContext ctx)
			{
			ctx.writeAndFlush(kryos.hash());
			}

		@Override
		public void channelInactive(ChannelHandlerContext ctx) throws Exception
			{
			}

		@Override
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
