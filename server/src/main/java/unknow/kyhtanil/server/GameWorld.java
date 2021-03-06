package unknow.kyhtanil.server;

import java.io.DataInputStream;
import java.io.FileInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.link.EntityLinkManager;

import unknow.kyhtanil.common.maps.MapLayout;
import unknow.kyhtanil.server.component.Archetypes;
import unknow.kyhtanil.server.manager.LocalizedManager;
import unknow.kyhtanil.server.manager.StateManager;
import unknow.kyhtanil.server.manager.UUIDManager;
import unknow.kyhtanil.server.system.DamageSystem;
import unknow.kyhtanil.server.system.DebugSystem;
import unknow.kyhtanil.server.system.DirtySystem;
import unknow.kyhtanil.server.system.MovementSystem;
import unknow.kyhtanil.server.system.ProjectileSystem;
import unknow.kyhtanil.server.system.SpawnSystem;
import unknow.kyhtanil.server.system.SpawnedSystem;
import unknow.kyhtanil.server.system.TtlSystem;
import unknow.kyhtanil.server.system.UpdateStatSystem;
import unknow.kyhtanil.server.system.net.AttackSystem;
import unknow.kyhtanil.server.system.net.Clients;
import unknow.kyhtanil.server.system.net.CreateAccountSystem;
import unknow.kyhtanil.server.system.net.CreateCharSystem;
import unknow.kyhtanil.server.system.net.LogCharSystem;
import unknow.kyhtanil.server.system.net.LoginSystem;
import unknow.kyhtanil.server.system.net.UpdateInfoSystem;
import unknow.kyhtanil.server.system.net.Server;

/**
 * handle the game world
 * 
 * @author unknow
 */
public class GameWorld {
	private static final Logger log = LoggerFactory.getLogger(GameWorld.class);

	private final World world;

	/**
	 * create new GameWorld
	 * 
	 * @throws Exception
	 */
	public GameWorld() throws Exception {
		MapLayout layout = new MapLayout(new DataInputStream(new FileInputStream("data/maps.layout")));

		WorldConfiguration cfg = new WorldConfiguration();
		cfg.setSystem(new EntityLinkManager());
		cfg.setSystem(new DebugSystem());
		cfg.setSystem(new Database());

		cfg.setSystem(new UUIDManager());
		cfg.setSystem(new LocalizedManager(10f, 10f));
		cfg.setSystem(new StateManager());
		cfg.setSystem(new Archetypes());

		cfg.setSystem(new Server(Cfg.port));
		cfg.setSystem(new Clients());

		cfg.setSystem(new LoginSystem());
		cfg.setSystem(new CreateAccountSystem());
		cfg.setSystem(new LogCharSystem());
		cfg.setSystem(new CreateCharSystem());
		cfg.setSystem(new UpdateInfoSystem());
		cfg.setSystem(new AttackSystem());

		cfg.setSystem(new SpawnSystem());
		cfg.setSystem(new SpawnedSystem());

		cfg.setSystem(new UpdateStatSystem());
		cfg.setSystem(new MovementSystem());
		cfg.setSystem(new ProjectileSystem());
		cfg.setSystem(new DamageSystem());
		cfg.setSystem(new DirtySystem());

		cfg.setSystem(new TtlSystem());

		cfg.register(layout);

		world = new World(cfg);
		world.getSystem(Database.class).init();
	}

	/**
	 * run the world
	 */
	public void run() {
		long start = System.currentTimeMillis();

		while (true) {
			try {
				long s = System.currentTimeMillis();
				world.delta = (s - start) / 1000f;
				world.process();
				start = s;
				Thread.sleep(1);
			} catch (Exception e) {
				log.error("", e);
			}
		}
	}

	/**
	 * @param arg
	 * @throws Exception
	 */
	public static void main(String arg[]) throws Exception {
		GameWorld world = new GameWorld();
		world.run();
	}
}
