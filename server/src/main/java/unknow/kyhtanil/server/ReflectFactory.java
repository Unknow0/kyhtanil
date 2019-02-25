package unknow.kyhtanil.server;

import com.artemis.*;

import unknow.kyhtanil.common.component.*;
import unknow.orm.reflect.*;

public class ReflectFactory extends unknow.orm.reflect.ReflectFactory
	{
	public static final ThreadLocal<Integer> entity=new ThreadLocal<Integer>();

	public static World world;

	public ReflectFactory()
		{
		}

	@SuppressWarnings({"unchecked", "rawtypes"})
	public <T> Instantiator<T> createInstantiator(Class<T> clazz)
		{
		if(clazz.equals(Body.class))
			return new Artemis(clazz, world);
		return super.createInstantiator(clazz);
		}

	private final class Artemis<T extends Component> extends Instantiator<T>
		{
		BaseComponentMapper<T> mapper;

		protected Artemis(Class<T> clazz, World world)
			{
			super(clazz);
			mapper=BaseComponentMapper.getFor(clazz, world);
			}

		public T newInstance()
			{
			return mapper.get(entity.get());
			}
		}
	}
