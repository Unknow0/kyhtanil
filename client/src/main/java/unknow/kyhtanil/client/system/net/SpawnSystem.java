package unknow.kyhtanil.client.system.net;

import org.slf4j.*;

import unknow.kyhtanil.client.*;
import unknow.kyhtanil.client.artemis.*;
import unknow.kyhtanil.common.component.*;
import unknow.kyhtanil.common.component.net.*;
import unknow.kyhtanil.common.pojo.*;

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

		Gdx.app.postRunnable(new MobLoader(s, s.type));
		}

	private class MobLoader implements Runnable
		{
		String tex;
		float x, y;
		UUID uuid;
		CalculatedComp total;

		public MobLoader(Spawn s, String tex)
			{
			this.uuid=s.uuid;
			this.x=s.x;
			this.y=s.y;
			this.total=s.total;
			this.tex=tex;
			}

		public void run()
			{
			TextureRegion mobTex=new TextureRegion(new Texture(Gdx.files.internal(tex)));

			int mob=Builder.buildMob(x, y, mobTex, Main.pixelToUnit(mobTex.getRegionWidth()), Main.pixelToUnit(mobTex.getRegionHeight()), total);
			manager.setUuid(mob, uuid);
			}
		}
	}
