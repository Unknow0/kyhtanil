package unknow.kyhtanil.server.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

import unknow.kyhtanil.common.Stats;
import unknow.kyhtanil.common.component.StatAgg;
import unknow.kyhtanil.common.component.StatBase;
import unknow.kyhtanil.common.component.StatShared;
import unknow.kyhtanil.server.component.Dirty;
import unknow.kyhtanil.server.component.StatModAggregator;

public class UpdateStatSystem extends IteratingSystem {
	private ComponentMapper<StatShared> shared;
	private ComponentMapper<StatBase> base;
	private ComponentMapper<StatModAggregator> aggregator;
	private ComponentMapper<StatAgg> calculated;
	private ComponentMapper<Dirty> dirty;

	public UpdateStatSystem() {
		super(Aspect.all(Dirty.class, StatShared.class, StatAgg.class, StatBase.class, StatModAggregator.class));
	}

	@Override
	protected void initialize() {
	}

	private boolean d = false;

	@Override
	public void process(int e) {
		d = false;
		StatShared info = shared.get(e);
		StatModAggregator stat = aggregator.get(e);

		StatBase b = base.get(e);

		StatAgg calc = calculated.get(e);

		for (Stats s : Stats.values())
			calc.set(s, total(calc.get(s), stat, s, base(s, b)));

		if (d) {
			d = false;
			dirty.get(e).add(calc);
		}

		info.maxHp = total(info.maxHp, stat, Stats.HP_MAX, Stats.baseHp(calc.get(Stats.STAT_CONSTITUTION)));
		info.maxMp = total(info.maxMp, stat, Stats.MP_MAX, Stats.baseMp(calc.get(Stats.STAT_INTELLIGENCE)));

		// TODO scale hp/mp

		if (d)
			dirty.get(e).add(info);
	}

	private static int base(Stats s, StatBase b) {
		switch (s) {
		case STAT_CONCENTRATION:
			return b.concentration;
		case STAT_CONSTITUTION:
			return b.constitution;
		case STAT_DEXTERITY:
			return b.dexterity;
		case STAT_INTELLIGENCE:
			return b.intelligence;
		case STAT_STRENGTH:
			return b.strength;
		case MOVE_SPEED:
			return 200;
		default:
			return 0;
		}
	}

	private int total(int last, StatModAggregator a, Stats s, int b) {
		int n = (int) ((b + a.flat.get(s)) * a.add.get(s) * a.more.get(s));
		if (last != n)
			d = true;
		return n;
	}
}