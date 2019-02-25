package unknow.kyhtanil.server.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

import unknow.kyhtanil.common.component.Body;
import unknow.kyhtanil.common.component.CalculatedComp;
import unknow.kyhtanil.common.component.MobInfoComp;

public class UpdateStatSystem extends IteratingSystem
	{
	private ComponentMapper<MobInfoComp> mobInfo;
	private ComponentMapper<Body> body;
	private ComponentMapper<CalculatedComp> calculated;

	public UpdateStatSystem()
		{
		super(Aspect.all(MobInfoComp.class, CalculatedComp.class));
		}

	@Override
	protected void initialize()
		{
		}

	@Override
	public void process(int e)
		{
		MobInfoComp info=mobInfo.get(e);
		Body b=body.get(e);
		CalculatedComp calc=calculated.get(e);

		calc.hp=info.hp;
		calc.mp=info.mp;

		calc.maxHp=b.constitution*15+10;
		calc.maxMp=b.intelligence*9+10;

		calc.strength=b.strength;
		calc.constitution=b.constitution;
		calc.intelligence=b.intelligence;
		calc.concentration=b.concentration;
		calc.dexterity=b.dexterity;

		calc.moveSpeed=2.5f;

		calc.dmg.set(1, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		// TODO add buff
		}
	}
