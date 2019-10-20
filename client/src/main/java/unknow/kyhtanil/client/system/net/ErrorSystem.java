package unknow.kyhtanil.client.system.net;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

import unknow.kyhtanil.client.Main;
import unknow.kyhtanil.common.component.ErrorComp;

public class ErrorSystem extends IteratingSystem
	{
	private ComponentMapper<ErrorComp> error;
	private Main main;

	public ErrorSystem(Main main)
		{
		super(Aspect.all(ErrorComp.class));
		this.main=main;
		}

	protected void process(int entityId)
		{
		ErrorComp e=error.get(entityId);
		world.delete(entityId);

		switch (e.code)
			{
			case INVALID_LOGIN:
				main.login.setError("Login/pass error");
				break;
			case ALREADY_LOGGED:
				main.login.setError("Account already logged");
				break;
			case UNKNOWN_ERROR:
			default:
				main.login.setError("Unknown error occured");
			}
		}
	}
