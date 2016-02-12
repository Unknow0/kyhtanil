package unknow.kyhtanil.server.utils;

import java.util.concurrent.*;

import org.apache.logging.log4j.*;
import org.objenesis.instantiator.*;
import org.objenesis.strategy.*;

import com.artemis.*;
import com.esotericsoftware.kryo.*;

public class ArtemisInstantiator implements InstantiatorStrategy
	{
	private static final Logger log=LogManager.getFormatterLogger(ArtemisInstantiator.class);
	private static final ThreadLocal<Integer> lastCreated=new ThreadLocal<Integer>();
	private final ConcurrentHashMap<Class<?>,ObjectInstantiator<?>> cache=new ConcurrentHashMap<>();
	private final World world;
	private final Class<? extends Component>[] additionalComp;
	private final InstantiatorStrategy defauldInstantiator=new Kryo.DefaultInstantiatorStrategy();

	@SafeVarargs
	public ArtemisInstantiator(World world, Class<? extends Component>... additionalComp)
		{
		this.world=world;
		this.additionalComp=additionalComp;
		}

	@SuppressWarnings("unchecked")
	public <T> ObjectInstantiator<T> newInstantiatorOf(Class<T> type)
		{
		ObjectInstantiator<?> o=cache.get(type);
		if(o==null)
			{
			if(Component.class.isAssignableFrom(type))
				o=new ArtemisObjectInstantiator(type.asSubclass(Component.class));
			else
				o=defauldInstantiator.newInstantiatorOf(type);
			cache.put(type, o);
			}
		return (ObjectInstantiator<T>)o;
		}

	public static Integer lastCreated()
		{
		return lastCreated.get();
		}

	private class ArtemisObjectInstantiator implements ObjectInstantiator<Object>
		{
		private Archetype arch;
		private ComponentMapper<?> mapper;

		public ArtemisObjectInstantiator(Class<? extends Component> comp)
			{
			ArchetypeBuilder builder=new ArchetypeBuilder().add(comp);
			if(additionalComp!=null&&additionalComp.length>0)
				builder.add(additionalComp);
			arch=builder.build(world);
			mapper=ComponentMapper.getFor(comp, world);
			}

		public Object newInstance()
			{
			int e=world.create(arch);
			lastCreated.set(e);
			log.info("create %s", mapper.getType());
			return mapper.get(e);
			}
		}
	}
