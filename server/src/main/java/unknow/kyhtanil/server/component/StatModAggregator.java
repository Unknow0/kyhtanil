package unknow.kyhtanil.server.component;

import com.artemis.PooledComponent;

import unknow.common.data.FloatEnumMap;
import unknow.common.data.IntEnumMap;
import unknow.kyhtanil.common.Stats;

/**
 * aggregate stats modifier
 */
public class StatModAggregator extends PooledComponent {
	/** all flat value */
	public final IntEnumMap<Stats> flat = new IntEnumMap<>(Stats.class, 0);
	/** all increase value */
	public final FloatEnumMap<Stats> add = new FloatEnumMap<>(Stats.class, 1);
	/** all more value */
	public final FloatEnumMap<Stats> more = new FloatEnumMap<>(Stats.class, 1);

	@Override
	protected void reset() {
		flat.clear();
		add.clear();
		more.clear();
	}
}
