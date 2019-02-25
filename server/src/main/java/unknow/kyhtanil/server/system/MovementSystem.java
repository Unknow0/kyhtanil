package unknow.kyhtanil.server.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

import unknow.kyhtanil.common.component.PositionComp;
import unknow.kyhtanil.common.component.VelocityComp;
import unknow.kyhtanil.server.manager.LocalizedManager;

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
