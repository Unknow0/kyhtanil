package unknow.kyhtanil.server.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.PreserveProcessVisiblity;
import com.artemis.systems.IteratingSystem;

import unknow.kyhtanil.common.Stats;
import unknow.kyhtanil.common.component.Dirty;
import unknow.kyhtanil.common.component.StatAgg;
import unknow.kyhtanil.common.component.StatBase;
import unknow.kyhtanil.common.component.StatShared;
import unknow.kyhtanil.server.component.StatModAggregator;

/**
 * compute aggregated stats
 * 
 * @author unknow
 */
@PreserveProcessVisiblity
public class UpdateStatSystem extends IteratingSystem {
	private ComponentMapper<StatShared> shared;
	private ComponentMapper<StatBase> base;
	private ComponentMapper<StatModAggregator> aggregator;
	private ComponentMapper<StatAgg> calculated;
	private ComponentMapper<Dirty> dirty;

	/**
	 * create new UpdateStatSystem
	 */
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
			calc.set(s, total(s, calc, stat, b));

		if (d) {
			d = false;
			dirty.get(e).add(calc);
		}

		int oldMaxHp = info.maxHp;
		int oldMaxMp = info.maxMp;
		info.maxHp = calc.get(Stats.HP_MAX);
		info.maxMp = calc.get(Stats.MP_MAX);

		if (oldMaxHp > 0)
			info.hp = (int) (1. * info.hp * info.maxHp / oldMaxHp);
		if (oldMaxMp > 0)
			info.mp = (int) (1. * info.mp * info.maxMp / oldMaxMp);

		// TODO add partial value
		info.hp += calc.get(Stats.HP_REGEN);
		info.mp += calc.get(Stats.MP_REGEN);

		if (oldMaxHp != info.maxHp || oldMaxMp != info.maxMp)
			dirty.get(e).add(info);
	}

	private int total(Stats s, StatAgg last, StatModAggregator a, StatBase b) {
		int base;
		switch (s) {
			case STAT_CONCENTRATION:
				base = b.concentration;
				break;
			case STAT_CONSTITUTION:
				base = b.constitution;
				break;
			case STAT_DEXTERITY:
				base = b.dexterity;
				break;
			case STAT_INTELLIGENCE:
				base = b.intelligence;
				break;
			case STAT_STRENGTH:
				base = b.strength;
				break;
			case WPN_DMG_BLUNT:
				base = 2;
				break;
			case MOVE_SPEED:
				base = 20;
				break;
			case HP_MAX:
				base = Stats.baseHp(last.get(Stats.STAT_CONSTITUTION));
				break;
			case MP_MAX:
				base = Stats.baseMp(last.get(Stats.STAT_INTELLIGENCE));
				break;
			case MP_REGEN:
				base = last.get(Stats.MP_MAX) / 100;
			default:
				base = 0;
				break;
		}

		int n = (int) ((base + a.flat.get(s)) * a.add.get(s) * a.more.get(s));
		if (last.get(s) != n)
			d = true;
		return n;
	}
}