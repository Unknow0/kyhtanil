package unknow.kyhtanil.server.component;

import java.util.IdentityHashMap;
import java.util.Map;

import com.artemis.Component;
import com.artemis.PooledComponent;

import unknow.kyhtanil.common.component.Setable;

public class Dirty extends PooledComponent
	{
	public Map<Class<?>,Setable<?>> map=new IdentityHashMap<>();

	@Override
	public void reset()
		{
		map.clear();
		}

	public void add(Setable<?> c)
		{
		map.put(c.getClass(), c);
		}

	public Component[] changed(Class<?> exclude)
		{
		int i=map.size();
		if(map.containsKey(exclude))
			i--;
		Component[] t=new Component[i];
		i=0;
		for(Setable<?> c:map.values())
			{
			if(exclude!=c.getClass())
				t[i++]=(Component)c;
			}
		return t;
		}
	}
