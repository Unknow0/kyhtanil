package unknow.kyhtanil.server.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;

import unknow.kyhtanil.common.component.PositionComp;
import unknow.kyhtanil.common.component.VelocityComp;
import unknow.kyhtanil.common.component.net.Move;
import unknow.kyhtanil.common.pojo.UUID;
import unknow.kyhtanil.server.GameWorld;
import unknow.kyhtanil.server.manager.LocalizedManager;
import unknow.kyhtanil.server.manager.UUIDManager;

public class MovementSystem extends IteratingSystem
	{
	private UUIDManager uuidManager;
	private LocalizedManager locManager;
	private ComponentMapper<VelocityComp> velocity;
	private ComponentMapper<PositionComp> position;

	@Wire
	private GameWorld gameWorld;

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
		// notify move ?

		UUID uuid=uuidManager.getUuid(e);
		gameWorld.send(null, p.x, p.y, new Move(uuid, p.x, p.y, v.direction));
		}
	}
