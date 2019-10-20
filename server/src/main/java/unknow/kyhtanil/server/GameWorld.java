package unknow.kyhtanil.server;

import java.io.DataInputStream;
import java.io.FileInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.Aspect;
import com.artemis.BaseComponentMapper;
import com.artemis.World;
import com.artemis.WorldConfiguration;

import unknow.kyhtanil.common.maps.MapLayout;
import unknow.kyhtanil.server.component.SpawnerComp;
import unknow.kyhtanil.server.manager.LocalizedManager;
import unknow.kyhtanil.server.manager.StateManager;
import unknow.kyhtanil.server.manager.UUIDManager;
import unknow.kyhtanil.server.system.DamageSystem;
import unknow.kyhtanil.server.system.DebugSystem;
import unknow.kyhtanil.server.system.DirtySystem;
import unknow.kyhtanil.server.system.EventSystem;
import unknow.kyhtanil.server.system.MovementSystem;
import unknow.kyhtanil.server.system.ProjectileSystem;
import unknow.kyhtanil.server.system.SpawnSystem;
import unknow.kyhtanil.server.system.UpdateStatSystem;
import unknow.kyhtanil.server.system.net.AttackSystem;
import unknow.kyhtanil.server.system.net.Clients;
import unknow.kyhtanil.server.system.net.LogCharSystem;
import unknow.kyhtanil.server.system.net.LoginSystem;
import unknow.kyhtanil.server.system.net.MoveSystem;
import unknow.kyhtanil.server.system.net.Server;
import unknow.kyhtanil.server.utils.Archetypes;

public class GameWorld
	{
	private static final Logger log=LoggerFactory.getLogger(GameWorld.class);

	private final Database database;

	private final World world;
	private final UUIDManager uuidManager;
	private final LocalizedManager locManager;

	private final BaseComponentMapper<SpawnerComp> spawner;

	private final Archetype spawnArch;

	public GameWorld() throws Exception
		{
		database=new Database();
		uuidManager=new UUIDManager();
		locManager=new LocalizedManager(10f, 10f);
		MapLayout layout=new MapLayout(new DataInputStream(new FileInputStream("data/maps.layout")));

		ApiWorld apiWorld=new ApiWorld();

		WorldConfiguration cfg=new WorldConfiguration();
		cfg.setSystem(DebugSystem.class);
		cfg.setSystem(database);

		cfg.setSystem(uuidManager);
		cfg.setSystem(locManager);
		cfg.setSystem(new StateManager());

		cfg.setSystem(new Server());
		cfg.setSystem(new Clients());

		cfg.setSystem(new EventSystem(Aspect.all()));
		cfg.setSystem(new LoginSystem());
		cfg.setSystem(new LogCharSystem());
		cfg.setSystem(new MoveSystem(this));
		cfg.setSystem(new AttackSystem());

		cfg.setSystem(new UpdateStatSystem());
		cfg.setSystem(new SpawnSystem());
		cfg.setSystem(new DamageSystem());
		cfg.setSystem(new MovementSystem());
		cfg.setSystem(new ProjectileSystem());

		cfg.setSystem(new DirtySystem());

		cfg.register(layout);
		cfg.register("javax.script.ScriptEngine", apiWorld.js());
		cfg.register(apiWorld);

		world=new World(cfg);
		world.inject(apiWorld);

		spawner=BaseComponentMapper.getFor(SpawnerComp.class, world);

		spawnArch=new ArchetypeBuilder().add(SpawnerComp.class).build(world);

		createSpawner(10, 10, 10, 3, 1);

		Archetypes.init(world);
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
				long s=System.currentTimeMillis();
				world.setDelta((s-start)/1000f);
				world.process();
				start=s;
				Thread.sleep(1);
				}
			catch (Exception e)
				{
				log.error("", e);
				}
			}
		}

	public static void main(String arg[]) throws Exception
		{
		GameWorld world=new GameWorld();
		world.run();
		}
	}
