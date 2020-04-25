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

	public PjInfoSystem(Main main) {
		super(Aspect.all(PjInfo.class));
		this.main = main;
	}

	@Override
	protected void process(int entityId) {
		PjInfo pj = pjInfo.get(entityId);

		world.delete(entityId);

		state.entity = world.create(arch.self);
		manager.setUuid(state.entity, state.uuid);

		Velocity v = velocity.get(state.entity);
		v.speed = 0f;

		Position p = position.get(state.entity);
		p.x = pj.x;
		p.y = pj.y;

		Sprite s = sprite.get(state.entity);
		s.tex = "char";
		s.w = s.h = 16;

		StatShared c = info.get(state.entity);
		c.set(pj.stats);

		StatBase d = perso.get(state.entity);
		d.set(pj.perso);

		main.show(Screen.GAME);
	}
}
