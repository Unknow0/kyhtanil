package unknow.kyhtanil.server;

import java.sql.*;

import javax.naming.*;
import javax.script.*;

import org.slf4j.*;

import unknow.common.*;
import unknow.json.*;
import unknow.kyhtanil.common.*;
import unknow.kyhtanil.common.component.*;
import unknow.kyhtanil.common.pojo.*;
import unknow.kyhtanil.server.component.*;
import unknow.kyhtanil.server.manager.*;
import unknow.kyhtanil.server.system.*;
import unknow.kyhtanil.server.system.net.*;
import unknow.kyhtanil.server.utils.*;
import unknow.orm.reflect.*;

import com.artemis.*;
import com.artemis.utils.*;

public class GameWorld
	{
	private static final Logger log=LoggerFactory.getLogger(GameWorld.class);

	private final Database database;

	private final World world;
	private final UUIDManager uuidManager;
	private final LocalizedManager locManager;

	private final ComponentMapper<StateComp> state;
	private final ComponentMapper<PositionComp> position;
	private final ComponentMapper<VelocityComp> velocity;
	private final ComponentMapper<MobInfoComp> mobInfo;
	private final ComponentMapper<SpawnerComp> spawner;
	private final ComponentMapper<CalculatedComp> calculated;

	private final Archetype spawnArch;

	private final ScriptEngine js;

	public GameWorld() throws ClassNotFoundException, ClassCastException, InstantiationException, IllegalAccessException, JsonException, SQLException, NamingException, ReflectException, ScriptException
		{
		js=new ScriptEngineManager().getEngineByName("javascript");

		database=new Database();
		uuidManager=new UUIDManager(this);
		locManager=new LocalizedManager(10f, 10f);

		AttackSystem attackSystem=new AttackSystem();
		ApiWorld apiWorld=new ApiWorld();

		WorldConfiguration cfg=new WorldConfiguration();
		cfg.setSystem(uuidManager);
		cfg.setSystem(locManager);
		cfg.setSystem(new StateManager());

		cfg.setSystem(new EventSystem(Aspect.all()));
		cfg.setSystem(new LoginSystem(database));
		cfg.setSystem(new LogCharSystem(database, this));
		cfg.setSystem(new MoveSystem(this));
		cfg.setSystem(attackSystem);

		cfg.setSystem(new UpdateStatSystem());
		cfg.setSystem(new SpawnSystem());
		cfg.setSystem(new DamageSystem(this));

		cfg.register(database);
		cfg.register("javax.script.ScriptEngine", js);
		cfg.register(apiWorld);

		world=new World(cfg);
		world.inject(database);
		world.inject(apiWorld);

		state=ComponentMapper.getFor(StateComp.class, world);
		position=ComponentMapper.getFor(PositionComp.class, world);
		velocity=ComponentMapper.getFor(VelocityComp.class, world);
		mobInfo=ComponentMapper.getFor(MobInfoComp.class, world);
		spawner=ComponentMapper.getFor(SpawnerComp.class, world);
		calculated=ComponentMapper.getFor(CalculatedComp.class, world);

		spawnArch=new ArchetypeBuilder().add(SpawnerComp.class).build(world);

		createSpawner(10, 10, 10, 3, 1);

		Archetypes.init(world);
		ReflectFactory.world=world;
		database.init();
		attackSystem.delayedInit();
		}

	private void createSpawner(float x, float y, float range, int max_count, float speed)
		{
		int e=world.create(spawnArch);
		SpawnerComp s=spawner.get(e);
		s.x=x;
		s.y=y;
		s.range=range;
		s.current_count=0;
		s.max_count=max_count;
		s.current=0;
		s.creation_speed=speed;
		}

	public World world()
		{
		return world;
		}

	public void run()
		{
		long start=System.currentTimeMillis();

		while (true)
			{
			try
				{
				world.setDelta((System.currentTimeMillis()-start)/1000f);
				world.process();
				start=System.currentTimeMillis();
				}
			catch (Exception e)
				{
				log.error("", e);
				}
			}
		}

	float range=50;

	public void send(StateComp sender, float x, float y, Object msg)
		{
		log.debug("send {} at {} x {}", msg, x, y);
		IntBag bag=locManager.get(x, y, range, state);

		for(int i=0; i<bag.size(); i++)
			{
			StateComp s=state.get(bag.get(i));
			if(s!=sender)
				{
				log.debug("	{} {}", s.account.getLogin(), uuidManager.getUuid(bag.get(i)));
				s.channel.writeAndFlush(msg);
				}
			}
		}

	public void despawn(StateComp sender, int entityId)
		{
		PositionComp p=position.get(entityId);
		UUID uuid=uuidManager.getUuid(entityId);
		send(sender, p.x, p.y, new Despawn(uuid));
		}

	public void spawn(StateComp sender, int entityId)
		{
		PositionComp p=position.get(entityId);
		VelocityComp v=velocity.get(entityId);
		MobInfoComp m=mobInfo.get(entityId);
		CalculatedComp c=calculated.get(entityId);
		UUID uuid=uuidManager.getUuid(entityId);
		send(sender, p.x, p.y, new Spawn(uuid, 0, m.name, c, p.x, p.y, v.direction));
		}

	private static Server server;

	public static void main(String arg[]) throws Exception
		{
		GameWorld world=new GameWorld();

		server=new Server(world.world());
		server.bind(Cfg.getSystemInt("kyhtanil.port"));

		world.run();
		}
	}
