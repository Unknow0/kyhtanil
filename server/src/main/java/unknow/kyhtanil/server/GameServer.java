package unknow.kyhtanil.server;

import io.netty.bootstrap.*;
import io.netty.buffer.*;
import io.netty.channel.*;
import io.netty.channel.nio.*;
import io.netty.channel.socket.*;
import io.netty.channel.socket.nio.*;
import io.netty.handler.codec.*;

import java.util.*;

import org.slf4j.*;

import unknow.common.tools.*;
import unknow.kyhtanil.common.component.*;
import unknow.kyhtanil.common.util.*;

import com.artemis.*;
import com.esotericsoftware.kryo.*;
import com.esotericsoftware.kryo.io.*;

public class GameServer
	{
	private static final Logger log=LoggerFactory.getLogger(GameServer.class);
	private static GameWorld world;
	private static ComponentMapper<NetComp> net;
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
				Integer e=ArtemisInstantiator.lastCreated();
				NetComp netComp=net.get(e);
				netComp.channel=ctx.channel();

				in.skipBytes(input.position());

				log.trace("read: {} {} {}", e, o.getClass(), JsonUtils.toString(o, true));
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

	public static class Handler extends ChannelHandlerAdapter
		{
		public void channelActive(ChannelHandlerContext ctx)
			{
//			ctx.writeAndFlush(new Message(version));
			}

		public void channelInactive(ChannelHandlerContext ctx) throws Exception
			{
			}

		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
			{
			}
		}

	public static GameWorld world()
		{
		return world;
		}

	public static void main(String arg[]) throws Exception
		{
		world=new GameWorld();
		world.start();

		net=ComponentMapper.getFor(NetComp.class, world.world());

		kryos=new Kryos(world.world(), NetComp.class);

		EventLoopGroup bossGroup=new NioEventLoopGroup();
		EventLoopGroup workerGroup=new NioEventLoopGroup();
		try
			{
			ServerBootstrap b=new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>()
				{ // (4)
					@Override
					public void initChannel(SocketChannel ch) throws Exception
						{
						ch.pipeline().addLast(new Decoder(), new Encoder(), new Handler());
						}
				}).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);

			// Bind and start to accept incoming connections.
			ChannelFuture f=b.bind(54320).sync(); // TODO

			f.channel().closeFuture().sync();
			}
		finally
			{
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
			}
		}
	}
