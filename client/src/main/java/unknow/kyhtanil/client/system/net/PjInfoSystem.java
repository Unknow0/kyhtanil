package unknow.kyhtanil.client.system.net;

import unknow.kyhtanil.client.*;
import unknow.kyhtanil.client.artemis.*;
import unknow.kyhtanil.client.component.*;
import unknow.kyhtanil.client.screen.*;
import unknow.kyhtanil.common.*;
import unknow.kyhtanil.common.component.*;

import com.artemis.*;
import com.artemis.systems.*;

public class PjInfoSystem extends IteratingSystem
	{
	private ComponentMapper<PjInfo> pjInfo;
	private ComponentMapper<BooleanComp> done;
	private Main main;
	private WorldScreen worldScreen;

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

		main.show(worldScreen);
		State.pj=pj;
		PositionComp p=Builder.getPosition(State.entity);
		p.x=State.pj.x;
		p.y=State.pj.y;
		}
	}
