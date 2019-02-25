package unknow.kyhtanil.client.system.net;

import unknow.kyhtanil.client.*;
import unknow.kyhtanil.client.artemis.Builder;
import unknow.kyhtanil.client.screen.*;
import unknow.kyhtanil.common.component.*;
import unknow.kyhtanil.common.component.account.*;

import com.artemis.*;
import com.artemis.systems.*;

public class PjInfoSystem extends IteratingSystem
	{
	private ComponentMapper<PjInfo> pjInfo;
	private ComponentMapper<BooleanComp> done;
	private Main main;
	private WorldScreen worldScreen;

	private ComponentMapper<PositionComp> position;
	private ComponentMapper<SpriteComp> sprite;
	private ComponentMapper<CalculatedComp> calculated;

	public PjInfoSystem(Main main, WorldScreen worldScreen)
		{
		super(Aspect.all(PjInfo.class, BooleanComp.class));
		this.main=main;
		this.worldScreen=worldScreen;
		}

	protected void process(int entityId)
		{
		PjInfo pj=pjInfo.get(entityId);
		BooleanComp b=done.get(entityId);
		if(!b.value) // entity not finished to be created
			return;

		world.delete(entityId);

		State.entity=world.create(Builder.pjArch);
		VelocityComp v=Builder.getVelocity(State.entity);
		v.speed=0f;
		PositionComp p=position.get(State.entity);
		p.x=pj.x;
		p.y=pj.y;
		SpriteComp s=sprite.get(State.entity);
		s.tex="data/tex/char.png";
		s.w=s.h=3;
		CalculatedComp c=calculated.get(State.entity);
		c.set(pj.total);
		State.stat=c;

		main.show(worldScreen);
		}
	}
