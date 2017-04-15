package unknow.kyhtanil.server;

import com.artemis.*;
import com.artemis.systems.*;

import unknow.common.*;
import unknow.kyhtanil.common.*;
import unknow.kyhtanil.common.component.*;
import unknow.kyhtanil.common.pojo.*;

public class MasterNode
	{
	private static Server server;

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
