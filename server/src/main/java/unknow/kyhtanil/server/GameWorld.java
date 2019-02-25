package unknow.kyhtanil.server;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.Aspect;
import com.artemis.BaseComponentMapper;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.utils.IntBag;

import unknow.common.Cfg;
import unknow.json.JsonException;
import unknow.kyhtanil.common.component.CalculatedComp;
import unknow.kyhtanil.common.component.MobInfoComp;
import unknow.kyhtanil.common.component.PositionComp;
import unknow.kyhtanil.common.component.SpriteComp;
import unknow.kyhtanil.common.component.VelocityComp;
import unknow.kyhtanil.common.component.net.Despawn;
import unknow.kyhtanil.common.component.net.Spawn;
import unknow.kyhtanil.common.maps.MapLayout;
import unknow.kyhtanil.common.pojo.UUID;
import unknow.kyhtanil.server.component.SpawnerComp;
import unknow.kyhtanil.server.component.StateComp;
import unknow.kyhtanil.server.manager.LocalizedManager;
import unknow.kyhtanil.server.manager.StateManager;
import unknow.kyhtanil.server.manager.UUIDManager;
import unknow.kyhtanil.server.system.DamageSystem;
import unknow.kyhtanil.server.system.DebugSystem;
import unknow.kyhtanil.server.system.EventSystem;
import unknow.kyhtanil.server.system.ProjectileSystem;
import unknow.kyhtanil.server.system.SpawnSystem;
import unknow.kyhtanil.server.system.UpdateStatSystem;
import unknow.kyhtanil.server.system.net.AttackSystem;
import unknow.kyhtanil.server.system.net.LogCharSystem;
import unknow.kyhtanil.server.system.net.LoginSystem;
import unknow.kyhtanil.server.system.net.MoveSystem;
import unknow.kyhtanil.server.utils.Archetypes;
import unknow.orm.reflect.ReflectException;

public class GameWorld
	{
	private static final Logger log=LoggerFactory.getLogger(GameWorld.class);

	private final Database database;

	private final World world;
	private final UUIDManager uuidManager;
	private final LocalizedManager locManager;

	private final BaseComponentMapper<StateComp> state;
	private final BaseComponentMapper<PositionComp> position;
	private final BaseComponentMapper<VelocityComp> velocity;
	private final BaseComponentMapper<SpriteComp> sprite;
	private final BaseComponentMapper<MobInfoComp> mobInfo;
	private final BaseComponentMapper<SpawnerComp> spawner;
	private final BaseComponentMapper<CalculatedComp> calculated;

	private final Archetype spawnArch;

	public GameWorld() throws ClassNotFoundException, ClassCastException, InstantiationException, IllegalAccessException, JsonException, SQLException, NamingException, ReflectException, ScriptException, FileNotFoundException, IOException
		{
		database=new Database();
		uuidManager=new UUIDManager(this);
		locManager=new LocalizedManager(10f, 10f);
		MapLayout layout=new MapLayout(new DataInputStream(new FileInputStream("data/maps.layout")));

		AttackSystem attackSystem=new AttackSystem();

		WorldConfiguration cfg=new WorldConfiguration();
		cfg.setAlwaysDelayComponentRemoval(true);
		cfg.setSystem(DebugSystem.class);

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
		cfg.setSystem(new ProjectileSystem());

		cfg.register(database);
		cfg.register(layout);
		cfg.register("javax.script.ScriptEngine", new ScriptEngineManager().getEngineByName("javascript"));

		world=new World(cfg);
		world.inject(database);

		ApiWorld é=new ApiWorld(world);

		state=BaseComponentMapper.getFor(StateComp.class, world);
		position=BaseComponentMapper.getFor(PositionComp.class, world);
		velocity=BaseComponentMapper.getFor(VelocityComp.class, world);
		sprite=BaseComponentMapper.getFor(SpriteComp.class, world);
		mobInfo=BaseComponentMapper.getFor(MobInfoComp.class, world);
		spawner=BaseComponentMapper.getFor(SpawnerComp.class, world);
		calculated=BaseComponentMapper.getFor(CalculatedComp.class, world);

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

	private final LocalizedManager.Choose filter=new LocalizedManager.Choose()
		{

		@Override
		public boolean choose(int e)
			{
			return state.has(e);
			}
		};

	public void send(StateComp sender, float x, float y, Object msg)
		{
		log.debug("send {} at {} x {} from {}", msg, x, y, sender);
		IntBag bag=locManager.get(x, y, range, filter);
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

	public void spawn(StateComp sender, int e)
		{
		PositionComp p=position.get(e);
		VelocityComp v=velocity.get(e);
		SpriteComp s=sprite.get(e);
		MobInfoComp m=mobInfo.get(e);
		CalculatedComp c=calculated.get(e);
		if(c!=null)
			m=new MobInfoComp(m, c);
		UUID uuid=uuidManager.getUuid(e);
		send(sender, p.x, p.y, new Spawn(uuid, s, m, p, v));
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
