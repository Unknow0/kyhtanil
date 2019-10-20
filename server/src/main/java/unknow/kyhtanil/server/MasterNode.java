package unknow.kyhtanil.server;

import unknow.kyhtanil.common.component.net.*;
import unknow.kyhtanil.common.pojo.*;

import com.artemis.*;
import com.artemis.systems.*;

public class MasterNode
	{

	private static class WhoIsSystem extends IteratingSystem
		{
		public WhoIsSystem()
			{
			super(Aspect.all(WhoIs.class, NetComp.class));
			}

		@Override
		protected void process(int entityId)
			{

			}
		}

	private static class WhoIs extends PooledComponent
		{
		public UUID uuid;

		@Override
		protected void reset()
			{
			uuid=null;
			}
		}

	private static class WhoIsRes extends PooledComponent
		{
		public UUID uuid;
		public String uri;

		@Override
		protected void reset()
			{
			uuid=null;
			uri=null;
			}
		}

	public static void main(String[] arg)
		{
//		server=new Server(world.world());
//		server.bind(54325);
		}

	}
