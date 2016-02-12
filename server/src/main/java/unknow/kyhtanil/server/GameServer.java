package unknow.kyhtanil.server;

import io.netty.bootstrap.*;
import io.netty.buffer.*;
import io.netty.channel.*;
import io.netty.channel.nio.*;
import io.netty.channel.socket.*;
import io.netty.channel.socket.nio.*;
import io.netty.handler.codec.*;

import java.util.*;

import org.apache.logging.log4j.*;

import unknow.common.tools.*;
import unknow.kyhtanil.server.component.*;
import unknow.kyhtanil.server.utils.*;

import com.artemis.*;
import com.esotericsoftware.kryo.*;
import com.esotericsoftware.kryo.io.*;

public class GameServer
	{
	private static final Logger log=LogManager.getFormatterLogger(GameServer.class);
	private static GameWorld world;
	private static ComponentMapper<NetComp> net;
	private static ThreadLocal<Kryo> kryos;

	public static class Decoder extends ByteToMessageDecoder
		{
		protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
			{
			try
				{
				byte[] dst=new byte[in.readableBytes()];
				in.readBytes(dst);

				Input input=new Input(dst);

				Kryo kryo=kryos.get();
				Object o=kryo.readClassAndObject(input);
				Integer e=ArtemisInstantiator.lastCreated();
				NetComp netComp=net.get(e);
				netComp.channel=ctx.channel();

				log.debug("read: %d %s %s", e, o.getClass(), StringTools.toJson(o, true));
				}
			catch (KryoException e)
				{
				e.printStackTrace();
				// TODO
				}
			}
		}

	public static class Encoder extends MessageToByteEncoder<Object>
		{
		protected void encode(ChannelHandlerContext ctx, Object data, ByteBuf out) throws Exception
			{
			log.debug("write: %s: %s", data.getClass(), StringTools.toJson(data, true));

			Kryo kryo=kryos.get();

			ByteBufOutputStream buf=new ByteBufOutputStream(out);
			Output output=new Output(buf);
			kryo.writeClassAndObject(output, data instanceof Null?null:data);
			output.close();
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
//			ServerWorld.self.close(ctx.channel());
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

		kryos=new ThreadLocal<Kryo>()
			{
				protected Kryo initialValue()
					{
					Kryo kryo=new Kryo();
					kryo.setReferences(false);

					kryo.setInstantiatorStrategy(new ArtemisInstantiator(world.world(), NetComp.class));

					return kryo;
					}
			};

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
