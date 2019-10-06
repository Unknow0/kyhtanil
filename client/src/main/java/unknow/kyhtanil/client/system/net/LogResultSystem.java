package unknow.kyhtanil.client.system.net;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

import unknow.kyhtanil.client.Main;
import unknow.kyhtanil.client.State;
import unknow.kyhtanil.client.screen.CharSelectScreen;
import unknow.kyhtanil.common.component.BooleanComp;
import unknow.kyhtanil.common.component.account.LogResult;

public class LogResultSystem extends IteratingSystem
	{
	private ComponentMapper<LogResult> logRes;
	private ComponentMapper<BooleanComp> done;
	private Main main;
	private CharSelectScreen charSelect;

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
		charSelect.setCharList(logResult.characters);
		main.show(charSelect);
		}
	}
