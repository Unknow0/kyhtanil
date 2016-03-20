package unknow.kyhtanil.server.system;

import unknow.kyhtanil.server.component.*;

import com.artemis.*;
import com.artemis.systems.*;

public class StateSystem extends IteratingSystem
	{
	private ComponentMapper<StateComp> state;

	public StateSystem()
		{
		super(Aspect.all(StateComp.class));
		}

	@Override
	protected void initialize()
		{
		}

	@Override
	protected void process(int e)
		{
		StateComp s=state.get(e);
		if(!s.channel.isActive())
			world.delete(e);
		}
	}
