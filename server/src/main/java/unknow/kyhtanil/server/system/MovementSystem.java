package unknow.kyhtanil.server.system;

import unknow.kyhtanil.common.component.*;
import unknow.kyhtanil.common.maps.*;
import unknow.kyhtanil.server.manager.*;

import com.artemis.*;
import com.artemis.systems.*;

public class MovementSystem extends IteratingSystem
	{
	private LocalizedManager locManager;
	private ComponentMapper<VelocityComp> velocity;
	private ComponentMapper<PositionComp> position;

	public MovementSystem()
		{
		super(Aspect.all(PositionComp.class, VelocityComp.class));
		}

	protected void initialize()
		{
		}

	@Override
	protected void process(int e)
		{
		VelocityComp v=velocity.get(e);
		if(v.speed==0)
			return;

		PositionComp p=position.get(e);
		p.x+=Math.cos(v.direction)*v.speed*world.delta;
		p.y+=Math.sin(v.direction)*v.speed*world.delta;
		locManager.changed(e);
		}
	}
