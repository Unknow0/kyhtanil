package unknow.kyhtanil.client.system.net;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

import unknow.kyhtanil.client.Main;
import unknow.kyhtanil.client.Main.Screen;
import unknow.kyhtanil.client.State;
import unknow.kyhtanil.client.component.Archetypes;
import unknow.kyhtanil.common.component.PositionComp;
import unknow.kyhtanil.common.component.SpriteComp;
import unknow.kyhtanil.common.component.StatPerso;
import unknow.kyhtanil.common.component.StatShared;
import unknow.kyhtanil.common.component.VelocityComp;
import unknow.kyhtanil.common.component.account.PjInfo;
import unknow.kyhtanil.common.util.BaseUUIDManager;

public class PjInfoSystem extends IteratingSystem
	{
	private ComponentMapper<PjInfo> pjInfo;
	private Main main;
	private BaseUUIDManager manager;

	private ComponentMapper<PositionComp> position;
	private ComponentMapper<VelocityComp> velocity;
	private ComponentMapper<SpriteComp> sprite;
	private ComponentMapper<StatShared> info;
	private ComponentMapper<StatPerso> perso;
	private Archetypes arch;

	public PjInfoSystem(Main main)
		{
		super(Aspect.all(PjInfo.class));
		this.main=main;
		}

	protected void process(int entityId)
		{
		PjInfo pj=pjInfo.get(entityId);

		world.delete(entityId);

		State.entity=world.create(arch.self);
		manager.setUuid(State.entity, State.uuid);

		VelocityComp v=velocity.get(State.entity);
		v.speed=0f;

		PositionComp p=position.get(State.entity);
		p.x=pj.x;
		p.y=pj.y;

		SpriteComp s=sprite.get(State.entity);
		s.tex="data/tex/char.png";
		s.w=s.h=3;

		StatShared c=info.get(State.entity);
		c.set(pj.stats);
		
		StatPerso d=perso.get(State.entity);
		d.set(pj.perso);

		State.stat=c;
		State.perso=d;

		main.show(Screen.GAME);
		}
	}
