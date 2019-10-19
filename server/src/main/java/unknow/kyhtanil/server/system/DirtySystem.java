package unknow.kyhtanil.server.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;

import unknow.kyhtanil.common.component.CalculatedComp;
import unknow.kyhtanil.common.component.PositionComp;
import unknow.kyhtanil.common.component.net.UpdateInfo;
import unknow.kyhtanil.common.pojo.UUID;
import unknow.kyhtanil.server.GameWorld;
import unknow.kyhtanil.server.component.Dirty;
import unknow.kyhtanil.server.manager.UUIDManager;

public class DirtySystem extends IteratingSystem
	{
	private ComponentMapper<Dirty> dirty;
	private ComponentMapper<PositionComp> pos;
	private ComponentMapper<CalculatedComp> info;

	private UUIDManager uuid;

	@Wire
	private GameWorld game;

	public DirtySystem()
		{
		super(Aspect.all(Dirty.class, PositionComp.class));
		}

	@Override
	protected void process(int e)
		{
		Dirty d=dirty.get(e);
		if(!d.dirty)
			return;
		d.dirty=false;

		UUID u=uuid.getUuid(e);
		if(u==null)
			return;

		PositionComp p=pos.get(e);
		CalculatedComp m=info.get(e);

		game.send(null, p.x, p.y, new UpdateInfo(u, p, m));
		}
	}
