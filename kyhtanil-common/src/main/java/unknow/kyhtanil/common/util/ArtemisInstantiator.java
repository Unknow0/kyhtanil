//package unknow.kyhtanil.common.util;
//
//import java.util.concurrent.*;
//
//import org.objenesis.instantiator.*;
//import org.objenesis.strategy.*;
//import org.slf4j.*;
//
//import unknow.common.*;
//
//import com.artemis.*;
//import com.esotericsoftware.kryo.*;
//
//public class ArtemisInstantiator implements InstantiatorStrategy {
//	private static final Logger log = LoggerFactory.getLogger(ArtemisInstantiator.class);
//	private static final ThreadLocal<Integer> lastCreated = new ThreadLocal<Integer>();
//	private final ConcurrentHashMap<Class<?>, ObjectInstantiator<?>> cache = new ConcurrentHashMap<>();
//	private final World world;
//	private final Class<? extends Component>[] additionalComp;
//	private final InstantiatorStrategy defauldInstantiator = new Kryo.DefaultInstantiatorStrategy();
//
//	@SafeVarargs
//	public ArtemisInstantiator(World world, Class<? extends Component>... additionalComp) {
//		this.world = world;
//		this.additionalComp = additionalComp;
//	}
//
//	@SuppressWarnings("unchecked")
//	public <T> ObjectInstantiator<T> newInstantiatorOf(Class<T> type) {
//		ObjectInstantiator<?> o = cache.get(type);
//		if (o == null) {
//			if (Component.class.isAssignableFrom(type))
//				o = new ArtemisObjectInstantiator(type.asSubclass(Component.class));
//			else
//				o = defauldInstantiator.newInstantiatorOf(type);
//			cache.put(type, o);
//		}
//		return (ObjectInstantiator<T>) o;
//	}
//
//	public static Integer lastCreated() {
//		return lastCreated.get();
//	}
//
//	public static void reset() {
//		lastCreated.set(null);
//	}
//
//	private class ArtemisObjectInstantiator implements ObjectInstantiator<Object> {
//		private Archetype arch;
//		private BaseComponentMapper<?> mapper;
//
//		public ArtemisObjectInstantiator(Class<? extends Component> comp) {
//			ArchetypeBuilder builder = new ArchetypeBuilder().add(comp);
//			if (additionalComp != null && additionalComp.length > 0)
//				builder.add(additionalComp);
//			arch = builder.build(world);
//			mapper = BaseComponentMapper.getFor(comp, world);
//		}
//
//		@SuppressWarnings("restriction")
//		public Object newInstance() {
//			try {
//				if (lastCreated.get() != null)
//					return Reflection.unsafe().allocateInstance(mapper.getType().getType());
//			} catch (InstantiationException e) {
//				throw new RuntimeException(e);
//			}
//
//			int e = world.create(arch);
//			lastCreated.set(e);
//			log.info("create entity {} {}", mapper.getType(), e);
//
//			return mapper.get(e);
//		}
//	}
//}
