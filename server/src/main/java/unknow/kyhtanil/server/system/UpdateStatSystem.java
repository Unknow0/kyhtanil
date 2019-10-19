package unknow.kyhtanil.server.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;

import unknow.kyhtanil.common.Stats;
import unknow.kyhtanil.common.component.AggregatedStat;
import unknow.kyhtanil.common.component.Body;
import unknow.kyhtanil.common.component.CalculatedComp;
import unknow.kyhtanil.common.component.MobInfoComp;
import unknow.kyhtanil.server.GameWorld;
import unknow.kyhtanil.server.component.Dirty;

public class UpdateStatSystem extends IteratingSystem
	{
	private ComponentMapper<MobInfoComp> mobInfo;
	private ComponentMapper<AggregatedStat> stats;
	private ComponentMapper<Body> body;
	private ComponentMapper<CalculatedComp> calculated;
	private ComponentMapper<Dirty> dirty;

	@Wire
	private GameWorld gameWorld;

	public UpdateStatSystem()
		{
		super(Aspect.all(MobInfoComp.class, CalculatedComp.class));
		}

	@Override
	protected void initialize()
		{
		}

	private boolean d=false;

	@Override
	public void process(int e)
		{
		d=false;
		MobInfoComp info=mobInfo.get(e);
		AggregatedStat stat=stats.get(e);

		Body b=body.get(e);

		CalculatedComp calc=calculated.get(e);

		calc.strength=total(calc.strength, stat, Stats.STAT_STRENGTH, b.strength);
		calc.constitution=total(calc.constitution, stat, Stats.STAT_CONSTITUTION, b.constitution);
		calc.intelligence=total(calc.intelligence, stat, Stats.STAT_INTELLIGENCE, b.intelligence);
		calc.concentration=total(calc.concentration, stat, Stats.STAT_CONCENTRATION, b.concentration);
		calc.dexterity=total(calc.dexterity, stat, Stats.STAT_DEXTERITY, b.dexterity);

		calc.hp=set(calc.hp, info.hp);
		calc.mp=set(calc.mp, info.mp);

		calc.maxHp=total(calc.maxHp, stat, Stats.HP_MAX, calc.constitution*15+10);
		calc.maxMp=total(calc.maxMp, stat, Stats.MP_MAX, calc.intelligence*9+10);

		calc.moveSpeed=2.5f;

		calc.dmg.set(1, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

		// TODO add buff

		// TODO only on change
		if(d)
			dirty.get(e).dirty=true;
		}

	private int set(int last, int n)
		{
		if(last!=n)
			d=true;
		return n;
		}

	private int total(int last, AggregatedStat a, Stats s, int b)
		{
		return set(last, (int)((b+a.flat.get(s))*a.add.get(s)*a.more.get(s)));
		}
	}