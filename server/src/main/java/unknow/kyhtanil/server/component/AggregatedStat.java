package unknow.kyhtanil.server.component;

import com.artemis.PooledComponent;

import unknow.common.data.FloatEnumMap;
import unknow.common.data.IntEnumMap;
import unknow.kyhtanil.common.Stats;

public class AggregatedStat extends PooledComponent
	{
	public final IntEnumMap<Stats> flat=new IntEnumMap<>(Stats.class, 0);
	public final FloatEnumMap<Stats> add=new FloatEnumMap<>(Stats.class, 1);
	public final FloatEnumMap<Stats> more=new FloatEnumMap<>(Stats.class, 1);

	@Override
	protected void reset()
		{
		flat.clear();
		add.clear();
		more.clear();
		}
	}
