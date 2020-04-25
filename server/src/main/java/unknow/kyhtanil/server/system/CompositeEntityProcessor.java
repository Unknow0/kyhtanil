package unknow.kyhtanil.server.system;

import java.util.*;

import unknow.kyhtanil.server.component.*;

import com.artemis.*;
import com.artemis.Aspect.Builder;
import com.artemis.annotations.*;
import com.artemis.utils.*;

@SkipWire
public abstract class CompositeEntityProcessor<T extends CompositeComponent<C>, C> extends EntitySystem {
	private BaseComponentMapper<T> comp;
	private Class<T> clazz;

	public CompositeEntityProcessor(Builder aspect, Class<T> comp) {
		super(aspect);
		clazz = comp;
	}

	@Override
	protected void initialize() {
		comp = BaseComponentMapper.getFor(clazz, world);
	}

	@Override
	protected void processSystem() {
		IntBag entities = subscription.getEntities();
		for (int i = 0; i < entities.size(); i++) {
			int e = entities.get(i);
			T t = comp.get(e);
			if (t.list.isEmpty())
				continue;
			boolean c = processStart(e);
			Iterator<C> it = t.list.iterator();
			while (c && it.hasNext())
				c = processComponent(e, it.next());
			processEnd(e);
			t.list.clear();
		}
	}

	/**
	 * notify the start of processing
	 * 
	 * @return true if we should process this entity
	 */
	protected abstract boolean processStart(int e);

	/**
	 * process a component of e
	 * 
	 * @return if we should continue to process
	 */
	protected abstract boolean processComponent(int e, C c);

	/**
	 * notify the end of processing
	 */
	protected abstract void processEnd(int e);
}
