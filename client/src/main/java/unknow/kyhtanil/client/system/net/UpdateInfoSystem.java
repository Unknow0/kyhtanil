package unknow.kyhtanil.client.system.net;

import org.slf4j.*;

import unknow.kyhtanil.client.State;
import unknow.kyhtanil.client.artemis.*;
import unknow.kyhtanil.common.component.*;
import unknow.kyhtanil.common.component.net.*;

import com.artemis.*;
import com.artemis.systems.*;

public class UpdateInfoSystem extends IteratingSystem
	{
	private static final Logger log=LoggerFactory.getLogger(UpdateInfoSystem.class);
	private ComponentMapper<UpdateInfo> update;
	private ComponentMapper<BooleanComp> done;

	private ComponentMapper<PositionComp> position;
	private ComponentMapper<MobInfoComp> info;
	private UUIDManager manager;

	public UpdateInfoSystem()
		{
		super(Aspect.all(UpdateInfo.class, BooleanComp.class));
		}

	protected void process(int entityId)
		{
		UpdateInfo u=update.get(entityId);
		BooleanComp b=done.get(entityId);
		if(!b.value) // entity not finished to be created
			return;
		world.delete(entityId);
		log.info("update {}", u);
		int e=manager.getEntity(u.uuid);
		log.info("=> {} / {}", e, State.entity);
		PositionComp p=position.get(e);
		p.x=u.x;
		p.y=u.y;
		MobInfoComp i=info.get(e);
		i.hp=u.hp;
		i.maxHp=u.maxHp;
		i.mp=u.mp;
		i.maxMp=u.maxMp;
		}
	}
