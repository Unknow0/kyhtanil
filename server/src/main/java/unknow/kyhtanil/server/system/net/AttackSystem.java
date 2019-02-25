package unknow.kyhtanil.server.system.net;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import com.esotericsoftware.kryo.util.IntMap;

import unknow.kyhtanil.common.Skill;
import unknow.kyhtanil.common.component.PositionComp;
import unknow.kyhtanil.common.component.net.Attack;
import unknow.kyhtanil.common.component.net.NetComp;
import unknow.kyhtanil.common.pojo.Point;
import unknow.kyhtanil.common.pojo.UUID;
import unknow.kyhtanil.server.Database;
import unknow.kyhtanil.server.manager.UUIDManager;

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
			Path path=Paths.get("data/skills");
			Files.walkFileTree(path, new FileVisitor<Path>()
				{
				@Override
				public FileVisitResult postVisitDirectory(Path arg0, IOException arg1) throws IOException
					{
					return FileVisitResult.CONTINUE;
					}

				@Override
				public FileVisitResult preVisitDirectory(Path arg0, BasicFileAttributes arg1) throws IOException
					{
					return FileVisitResult.CONTINUE;
					}

				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attr) throws IOException
					{

					try (Reader in=Files.newBufferedReader(file, StandardCharsets.UTF_8))
						{
						Skill eval=(Skill)js.eval(in);
						skills.put(eval.id, eval);
						}
					catch (ScriptException e)
						{
						throw new IOException(e);
						}
					return FileVisitResult.CONTINUE;
					}

				@Override
				public FileVisitResult visitFileFailed(Path arg0, IOException arg1) throws IOException
					{
					return FileVisitResult.CONTINUE;
					}
				});
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
