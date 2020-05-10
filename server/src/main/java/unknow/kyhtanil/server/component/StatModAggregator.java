package unknow.kyhtanil.server.component;

import com.artemis.PooledComponent;

import unknow.common.data.EnumFloatMap;
import unknow.common.data.EnumIntMap;
import unknow.kyhtanil.common.Stats;

/**
 * aggregate stats modifier
 */
public class StatModAggregator extends PooledComponent {
	/** all flat value */
	public final EnumIntMap<Stats> flat = new EnumIntMap<>(Stats.class, 0);
	/** all increase value */
	public final EnumFloatMap<Stats> add = new EnumFloatMap<>(Stats.class, 1);
	/** all more value */
	public final EnumFloatMap<Stats> more = new EnumFloatMap<>(Stats.class, 1);

	@Override
	protected void reset() {
		flat.clear();
		add.clear();
		more.clear();
	}
}
