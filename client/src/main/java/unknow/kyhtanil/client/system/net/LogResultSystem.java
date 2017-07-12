package unknow.kyhtanil.client.system.net;

import unknow.kyhtanil.client.*;
import unknow.kyhtanil.client.artemis.*;
import unknow.kyhtanil.client.screen.*;
import unknow.kyhtanil.common.component.*;
import unknow.kyhtanil.common.component.account.*;

import com.artemis.*;
import com.artemis.systems.*;

public class LogResultSystem extends IteratingSystem
	{
	private ComponentMapper<LogResult> logRes;
	private ComponentMapper<BooleanComp> done;
	private Main main;
	private CharSelectScreen charSelect;
	private UUIDManager manager;

	public LogResultSystem(Main main, CharSelectScreen charSelect)
		{
		super(Aspect.all(LogResult.class, BooleanComp.class));
		this.main=main;
		this.charSelect=charSelect;
		}

	protected void process(int entityId)
		{
		LogResult logResult=logRes.get(entityId);
		BooleanComp b=done.get(entityId);
		if(!b.value) // entity not finished to be created
			return;
		world.delete(entityId);

		State.uuid=logResult.uuid;
		manager.setUuid(State.entity, State.uuid);
		charSelect.setCharList(logResult.characters);
		main.show(charSelect);
		}
	}
