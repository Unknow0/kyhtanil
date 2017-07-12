package unknow.kyhtanil.server.system.net;

import java.io.*;
import java.util.regex.*;

import javax.script.*;

import org.slf4j.*;

import unknow.common.*;
import unknow.kyhtanil.common.*;
import unknow.kyhtanil.common.component.*;
import unknow.kyhtanil.common.component.net.*;
import unknow.kyhtanil.common.pojo.*;
import unknow.kyhtanil.server.*;
import unknow.kyhtanil.server.manager.*;

import com.artemis.*;
import com.artemis.annotations.*;
import com.artemis.systems.*;
import com.esotericsoftware.kryo.util.*;

public class AttackSystem extends IteratingSystem
	{
	private static final Logger log=LoggerFactory.getLogger(AttackSystem.class);

	private UUIDManager manager;
	private ComponentMapper<Attack> attack;
	private ComponentMapper<NetComp> net;
	private ComponentMapper<PositionComp> position;

	@Wire
	private ScriptEngine js;
	@Wire
	private Database database;

	private IntMap<Skill> skills=new IntMap<Skill>();

	public AttackSystem()
		{
		super(Aspect.all(Attack.class, NetComp.class));
		}

	public void delayedInit()
		{
		try
			{
			database.init();
			js.put("Skill", js.eval("Java.type('"+Skill.class.getName()+"');"));
			for(Resource r:Resource.findRessources(Pattern.compile("skills/.*.js")))
				{
				try (Reader in=new InputStreamReader(r.url.openStream(), "UTF8"))
					{
					Skill eval=(Skill)js.eval(in);
					skills.put(eval.id, eval);
					}
				}
			}
		catch (Exception e)
			{
			throw new RuntimeException("failled to init skills", e);
			}
		}

	protected void process(int entityId)
		{
		Attack a=attack.get(entityId);
		NetComp ctx=net.get(entityId);
		if(ctx.channel==null) // entity not finished to be created
			return;

		world.delete(entityId);
		Integer self=manager.getEntity(a.uuid);
		if(self==null)
			{
			ctx.channel.close();
			log.debug("failed to get State '{}' on attack", a.uuid);
			return;
			}

//		float range=5;
		Integer t=null;
//		PositionComp sp=position.get(self);
		Point p;
		if(a.target instanceof UUID)
			{
			t=manager.getEntity((UUID)a.target);
			if(t==null)
				return;
			PositionComp pos=position.get(t);
			p=new Point(pos.x, pos.y);
			}
		else
			p=(Point)a.target;

//		js.put("point", p);
//		js.put("target", t);
//		js.put("self", self);
		Skill script=skills.get(a.id);
		script.exec(self, p, t);
		}
	}
