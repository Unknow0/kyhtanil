package unknow.kyhtanil.server.system;

import unknow.kyhtanil.server.component.*;

import com.artemis.*;
import com.artemis.systems.*;

public class UpdateStatSystem extends IteratingSystem
	{
	private ComponentMapper<MobInfoComp> mobInfo;
	private ComponentMapper<CalculatedComp> calculated;

	public UpdateStatSystem()
		{
		super(Aspect.all(MobInfoComp.class, CalculatedComp.class));
		}

	@Override
	protected void initialize()
		{
		mobInfo=ComponentMapper.getFor(MobInfoComp.class, world);
		calculated=ComponentMapper.getFor(CalculatedComp.class, world);
		}

	@Override
	protected void process(int e)
		{
		MobInfoComp info=mobInfo.get(e);
		CalculatedComp calc=calculated.get(e);

		calc.maxHp=info.constitution*15+10;
		calc.maxMp=info.intelligence*9+10;

		}
	}
