package unknow.kyhtanil.client.system;

import unknow.kyhtanil.client.artemis.*;
import unknow.kyhtanil.common.component.*;

import com.artemis.*;
import com.artemis.systems.*;

public class MovementSystem extends IteratingSystem
	{
	public MovementSystem()
		{
		super(Aspect.all(PositionComp.class, VelocityComp.class));
		}

	@Override
	protected void initialize()
		{
		}

	@Override
	protected void process(int e)
		{
		VelocityComp v=Builder.getVelocity(e);
		if(v.speed==0)
			return;

		PositionComp p=Builder.getPosition(e);
		p.x+=Math.cos(v.direction)*v.speed*world.delta;
		p.y+=Math.sin(v.direction)*v.speed*world.delta;
		}
	}
