package unknow.kyhtanil.server.system.net;

import org.slf4j.*;

import unknow.kyhtanil.common.component.*;
import unknow.kyhtanil.common.component.account.*;
import unknow.kyhtanil.common.component.net.*;
import unknow.kyhtanil.common.pojo.*;
import unknow.kyhtanil.server.*;
import unknow.kyhtanil.server.component.*;
import unknow.kyhtanil.server.component.StateComp.States;
import unknow.kyhtanil.server.manager.*;
import unknow.kyhtanil.server.pojo.*;
import unknow.kyhtanil.server.utils.*;

import com.artemis.*;
import com.artemis.systems.*;

/**
 * manage the login request
 * <br>assign an UUID to the new State
 */
public class CreateAccountSystem extends IteratingSystem
	{
	private static final Logger log=LoggerFactory.getLogger(CreateAccountSystem.class);

	private StateManager manager;
	private ComponentMapper<CreateAccount> create;
	private ComponentMapper<NetComp> net;
	private ComponentMapper<StateComp> state;

	private Database database;

	public CreateAccountSystem(Database database)
		{
		super(Aspect.all(CreateAccount.class, NetComp.class));
		this.database=database;
		}

	@Override
	protected void process(int e)
		{
		CreateAccount l=create.get(e);
		NetComp ctx=net.get(e);
		if(ctx.channel==null) // entity not finished to be created
			return;

		world.delete(e);
		try
			{
			Account a=database.createAccount(l.login, l.passHash);
			if(a!=null)
				{
				int ns=world.create(Archetypes.pj);
				StateComp s=state.get(ns);
				s.account=a;
				s.channel=ctx.channel;
				s.state=States.LOGGED;

				UUID uuid=manager.log(ns, a.getLogin());
				if(uuid!=null)
					{
					log.info("log {} {}", s.account.getLogin(), uuid);
					ctx.channel.writeAndFlush(new LogResult(uuid, new CharDesc[0]));
					}
				else
					ctx.channel.writeAndFlush(ErrorComp.ALREADY_LOGGED);
				}
			else
				ctx.channel.writeAndFlush(ErrorComp.INVALID_LOGIN);
			}
		catch (Exception ex)
			{
			log.error("failed to log char", ex);
			ctx.channel.writeAndFlush(ErrorComp.UNKNOWN_ERROR);
			}
		}
	}
