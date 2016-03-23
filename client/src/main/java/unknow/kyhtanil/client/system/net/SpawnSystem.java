package unknow.kyhtanil.client.system.net;

import java.sql.*;

import org.slf4j.*;

import unknow.kyhtanil.client.*;
import unknow.kyhtanil.client.artemis.*;
import unknow.kyhtanil.common.*;
import unknow.kyhtanil.common.component.*;

import com.artemis.*;
import com.artemis.systems.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;

public class SpawnSystem extends IteratingSystem
	{
	private static final Logger log=LoggerFactory.getLogger(SpawnSystem.class);
	private ComponentMapper<Spawn> spawn;
	private ComponentMapper<BooleanComp> done;
	private UUIDManager manager;

	public SpawnSystem()
		{
		super(Aspect.all(Spawn.class, BooleanComp.class));
		}

	protected void process(int entityId)
		{
		Spawn s=spawn.get(entityId);
		BooleanComp b=done.get(entityId);
		if(!b.value) // entity not finished to be created
			return;
		world.delete(entityId);
		log.info("{}", s);

		final String tex;
		String t=null;
		try
			{
			t=Db.getMobTex(s.type); // TODO cache
			}
		catch (SQLException e)
			{
			e.printStackTrace();
			}
		if(t!=null)
			tex=t;
		else
			tex="mob.png";

		Gdx.app.postRunnable(new MobLoader(s, tex));
		}

	private class MobLoader implements Runnable
		{
		String tex;
		Spawn s;

		public MobLoader(Spawn s, String tex)
			{
			this.s=s;
			this.tex=tex;
			}

		public void run()
			{
			TextureRegion mobTex=new TextureRegion(new Texture(Gdx.files.internal(tex)));

			int mob=Builder.buildMob(s.x, s.y, mobTex, Main.pixelToUnit(mobTex.getRegionWidth()), Main.pixelToUnit(mobTex.getRegionHeight()), s.total);
			manager.setUuid(mob, s.uuid);
			}
		}
	}
