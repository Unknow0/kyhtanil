package unknow.kyhtanil.client.system.net;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

import unknow.kyhtanil.client.Main;
import unknow.kyhtanil.client.Main.Screen;
import unknow.kyhtanil.client.component.Archetypes;
import unknow.kyhtanil.client.system.State;
import unknow.kyhtanil.common.component.PositionComp;
import unknow.kyhtanil.common.component.SpriteComp;
import unknow.kyhtanil.common.component.StatBase;
import unknow.kyhtanil.common.component.StatShared;
import unknow.kyhtanil.common.component.VelocityComp;
import unknow.kyhtanil.common.component.account.PjInfo;
import unknow.kyhtanil.common.util.BaseUUIDManager;

public class PjInfoSystem extends IteratingSystem {
	private Main main;
	private State state;
	private BaseUUIDManager manager;

	private ComponentMapper<PjInfo> pjInfo;
	private ComponentMapper<PositionComp> position;
	private ComponentMapper<VelocityComp> velocity;
	private ComponentMapper<SpriteComp> sprite;
	private ComponentMapper<StatShared> info;
	private ComponentMapper<StatBase> perso;
	private Archetypes arch;

	public PjInfoSystem(Main main) {
		super(Aspect.all(PjInfo.class));
		this.main = main;
	}

	protected void process(int entityId) {
		PjInfo pj = pjInfo.get(entityId);

		world.delete(entityId);

		state.entity = world.create(arch.self);
		manager.setUuid(state.entity, state.uuid);

		VelocityComp v = velocity.get(state.entity);
		v.speed = 0f;

		PositionComp p = position.get(state.entity);
		p.x = pj.x;
		p.y = pj.y;

		SpriteComp s = sprite.get(state.entity);
		s.tex = "char";
		s.w = s.h = 24;

		StatShared c = info.get(state.entity);
		c.set(pj.stats);

		StatBase d = perso.get(state.entity);
		d.set(pj.perso);

		main.show(Screen.GAME);
	}
}
