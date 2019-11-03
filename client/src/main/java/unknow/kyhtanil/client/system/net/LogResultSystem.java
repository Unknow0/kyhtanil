package unknow.kyhtanil.client.system.net;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

import unknow.kyhtanil.client.Main;
import unknow.kyhtanil.client.State;
import unknow.kyhtanil.client.Main.Screen;
import unknow.kyhtanil.common.component.account.LogResult;

public class LogResultSystem extends IteratingSystem
	{
	private ComponentMapper<LogResult> logRes;
	private Main main;

	public LogResultSystem(Main main)
		{
		super(Aspect.all(LogResult.class));
		this.main=main;
		}

	protected void process(int entityId)
		{
		LogResult logResult=logRes.get(entityId);
		world.delete(entityId);

		State.uuid=logResult.uuid;
		State.chars=logResult.characters;
		main.show(Screen.CHARSELECT);
		}
	}
