package unknow.kyhtanil.server;

import java.sql.*;

import javax.naming.*;

import org.slf4j.*;

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

public class GameWorld extends Thread
	{
	private static final Logger log=LoggerFactory.getLogger(GameWorld.class);

	private Database database;

	private World world;
	private final UUIDManager uuidManager=new UUIDManager();
	private final LocalizedManager locManager=new LocalizedManager(10f, 10f);

	private final Mappers mappers;

	private final Archetype spawnArch;

	public GameWorld() throws ClassNotFoundException, ClassCastException, InstantiationException, IllegalAccessException, JsonException, SQLException, NamingException, ReflectException
		{
		database=new Database();

		WorldConfiguration cfg=new WorldConfiguration();
		cfg.setSystem(uuidManager);
		cfg.setSystem(locManager);
		cfg.setSystem(new StateManager());

		cfg.setSystem(new LoginSystem(database));
		cfg.setSystem(new LogCharSystem(database));
		cfg.setSystem(new MoveSystem());
		cfg.setSystem(new AttackSystem());

		cfg.setSystem(new UpdateStatSystem());
		cfg.setSystem(new StateSystem());
		cfg.setSystem(new SpawnSystem());
		cfg.setSystem(new DamageSystem());

		world=new World(cfg);

		mappers=new Mappers(world);

		spawnArch=new ArchetypeBuilder().add(SpawnerComp.class).build(world);

		createSpawner(10, 10, 10, 3, 1);

		Archetypes.init(world);
		ReflectFactory.world=world;
		database.init(mappers);
		}

	private void createSpawner(float x, float y, float range, int max_count, float speed)
		{
		int e=world.create(spawnArch);
		SpawnerComp s=mappers.spawner(e);
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

		while (!isInterrupted())
			{
			world.setDelta((System.currentTimeMillis()-start)/1000f);
			world.process();
			start=System.currentTimeMillis();
			}
		}

	float range=50;

	public void send(StateComp sender, float x, float y, Object msg)
		{
		log.debug("send {} at {} x {}", msg, x, y);
		IntBag bag=locManager.get(x, y, range, mappers.state());

		for(int i=0; i<bag.size(); i++)
			{
			StateComp s=mappers.state(bag.get(i));
			if(s!=sender)
				{
				log.debug("	{} {}", s.account.getLogin(), uuidManager.getUuid(bag.get(i)));
				s.channel.writeAndFlush(msg);
				}
			}
		}

	public void despawn(StateComp sender, int entityId)
		{
		PositionComp p=mappers.position(entityId);
		UUID uuid=uuidManager.getUuid(entityId);
		send(sender, p.x, p.y, new Despawn(uuid));
		}

	public void spawn(StateComp sender, int entityId)
		{
		PositionComp p=mappers.position(entityId);
		VelocityComp v=mappers.velocity(entityId);
		MobInfoComp m=mappers.mobInfo(entityId);
		CalculatedComp c=mappers.calculated(entityId);
		UUID uuid=uuidManager.getUuid(entityId);
		send(sender, p.x, p.y, new Spawn(uuid, 0, m.name, c, p.x, p.y, v.direction));
		}
	}
