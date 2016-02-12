package unknow.kyhtanil.server.system.net;

import java.util.*;

import org.apache.logging.log4j.*;

import unknow.kyhtanil.common.*;
import unknow.kyhtanil.common.UUID;
import unknow.kyhtanil.server.*;
import unknow.kyhtanil.server.component.*;
import unknow.kyhtanil.server.manager.*;
import unknow.kyhtanil.server.pojo.*;
import unknow.kyhtanil.server.utils.*;

import com.artemis.*;
import com.artemis.systems.*;

public class LoginSystem extends IteratingSystem
	{
	private static final Logger log=LogManager.getFormatterLogger(LoginSystem.class);

	private ComponentMapper<Login> login;
	private ComponentMapper<NetComp> net;
	private ComponentMapper<StateComp> state;

	private Archetype stateArch;

	private Database database;

	public LoginSystem(Database database)
		{
		super(Aspect.all(Login.class, NetComp.class));
		this.database=database;
		}

	@SuppressWarnings("unchecked")
	protected void initialize()
		{
//		login=ComponentMapper.getFor(Login.class, world);
//		net=ComponentMapper.getFor(NetComp.class, world);
//		state=ComponentMapper.getFor(StateComp.class, world);

		stateArch=new ArchetypeBuilder().add(PositionComp.class, VelocityComp.class, MobInfoComp.class, StateComp.class, DamageListComp.class, BuffListComp.class, CalculatedComp.class).build(world);
		}

	protected void process(int e)
		{
		Login l=login.get(e);
		NetComp ctx=net.get(e);
		if(ctx.channel==null) // entity not finished to be created
			return;

		world.delete(e);
		try
			{
			Account a=database.getAccount(l.login, l.pass);
			if(a!=null)
				{
				int ns=world.create(stateArch);
				log.info("login %s: %d", l.login, ns);
				StateComp s=state.get(ns);
				s.account=a;
				s.channel=ctx.channel;

				UUID uuid=UUIDManager.self().assignUuid(ns);
				List<CharDesc> charList=database.getCharList(a.getId());
				log.info("log %s %s", uuid, charList);
				ctx.channel.writeAndFlush(new LogResult(uuid, charList));
				}
			else
				ctx.channel.writeAndFlush(Null.get());
			}
		catch (Exception ex)
			{
			ex.printStackTrace();
			ctx.channel.writeAndFlush(Null.get());
			}
		}
	}
