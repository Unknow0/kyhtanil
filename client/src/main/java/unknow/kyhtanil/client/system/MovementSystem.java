package unknow.kyhtanil.client.system;

import unknow.kyhtanil.client.artemis.*;
import unknow.kyhtanil.client.component.*;

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
		if(v.dirX==0&&v.dirY==0)
			return;

		PositionComp p=Builder.getPosition(e);
		p.x+=v.dirX*world.delta;
		p.y+=v.dirY*world.delta;
		}
	}
