package unknow.kyhtanil.server.system;

import unknow.kyhtanil.server.component.*;
import unknow.kyhtanil.server.manager.*;

import com.artemis.*;
import com.artemis.systems.*;

public class MovementSystem extends IteratingSystem
	{
	private ComponentMapper<VelocityComp> velocity;
	private ComponentMapper<PositionComp> position;

	public MovementSystem()
		{
		super(Aspect.all(PositionComp.class, VelocityComp.class));
		}

	protected void initialize()
		{
		velocity=ComponentMapper.getFor(VelocityComp.class, world);
		position=ComponentMapper.getFor(PositionComp.class, world);
		}

	@Override
	protected void process(int e)
		{
		VelocityComp v=velocity.get(e);
		if(v.dirX==0&&v.dirY==0)
			return;

		PositionComp p=position.get(e);
		p.x+=v.dirX;
		p.y+=v.dirY;

		LocalizedManager.changed(e);
		}
	}
