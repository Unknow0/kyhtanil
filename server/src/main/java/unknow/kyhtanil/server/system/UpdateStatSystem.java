package unknow.kyhtanil.server.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

import unknow.kyhtanil.common.Stats;
import unknow.kyhtanil.common.component.Body;
import unknow.kyhtanil.common.component.StatPerso;
import unknow.kyhtanil.common.component.StatShared;
import unknow.kyhtanil.server.component.AggregatedStat;
import unknow.kyhtanil.server.component.Dirty;

public class UpdateStatSystem extends IteratingSystem
	{
	private ComponentMapper<StatShared> mobInfo;
	private ComponentMapper<AggregatedStat> stats;
	private ComponentMapper<Body> body;
	private ComponentMapper<StatPerso> calculated;
	private ComponentMapper<Dirty> dirty;

	public UpdateStatSystem()
		{
		super(Aspect.all(StatShared.class, StatPerso.class));
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
		StatShared info=mobInfo.get(e);
		AggregatedStat stat=stats.get(e);

		Body b=body.get(e);

		StatPerso calc=calculated.get(e);

		calc.strength=total(calc.strength, stat, Stats.STAT_STRENGTH, b.strength);
		calc.constitution=total(calc.constitution, stat, Stats.STAT_CONSTITUTION, b.constitution);
		calc.intelligence=total(calc.intelligence, stat, Stats.STAT_INTELLIGENCE, b.intelligence);
		calc.concentration=total(calc.concentration, stat, Stats.STAT_CONCENTRATION, b.concentration);
		calc.dexterity=total(calc.dexterity, stat, Stats.STAT_DEXTERITY, b.dexterity);

		calc.moveSpeed=total(calc.moveSpeed, stat, Stats.MOVE_SPEED, 250);

		calc.dmg.set(1, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

		if(d)
			{
			d=false;
			dirty.get(e).add(calc);
			}

//		info.hp=set(info.hp, info.hp);
//		info.mp=set(info.mp, info.mp);

		info.maxHp=total(info.maxHp, stat, Stats.HP_MAX, calc.constitution*15+10);
		info.maxMp=total(info.maxMp, stat, Stats.MP_MAX, calc.intelligence*9+10);

		if(d)
			dirty.get(e).add(info);
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