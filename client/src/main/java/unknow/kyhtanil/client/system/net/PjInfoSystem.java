package unknow.kyhtanil.client.system.net;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

import unknow.kyhtanil.client.Main;
import unknow.kyhtanil.client.Main.Screen;
import unknow.kyhtanil.client.component.Archetypes;
import unknow.kyhtanil.client.system.State;
import unknow.kyhtanil.common.component.Position;
import unknow.kyhtanil.common.component.Sprite;
import unknow.kyhtanil.common.component.StatBase;
import unknow.kyhtanil.common.component.StatShared;
import unknow.kyhtanil.common.component.Velocity;
import unknow.kyhtanil.common.component.account.PjInfo;
import unknow.kyhtanil.common.util.BaseUUIDManager;

/**
 * apply PjInfo (eg show game windows)
 * 
 * @author unknow
 */
public class PjInfoSystem extends IteratingSystem {
	private Main main;
	private State state;
	private BaseUUIDManager manager;

	private ComponentMapper<PjInfo> pjInfo;
	private ComponentMapper<Position> position;
	private ComponentMapper<Velocity> velocity;
	private ComponentMapper<Sprite> sprite;
	private ComponentMapper<StatShared> info;
	private ComponentMapper<StatBase> perso;
	private Archetypes arch;

	/**
	 * create new PjInfoSystem
	 * 
	 * @param main
	 */
	public PjInfoSystem(Main main) {
		super(Aspect.all(PjInfo.class));
		this.main = main;
	}

	@Override
	protected void process(int entityId) {
		PjInfo pj = pjInfo.get(entityId);

		world.delete(entityId);

		int e = state.entity = world.create(arch.self);
		manager.setUuid(e, state.uuid);

		Velocity v = velocity.get(e);
		v.speed = 0f;

		position.get(e).set(pj.p);

		Sprite s = sprite.get(e);
		s.tex = "char";
		s.w = s.h = 16;

		info.get(e).set(pj.stats);
		perso.get(e).set(pj.perso);

		main.show(Screen.GAME);
	}
}
