package unknow.kyhtanil.server.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.artemis.utils.IntBag;

import unknow.kyhtanil.common.component.PositionComp;
import unknow.kyhtanil.server.component.Projectile;
import unknow.kyhtanil.server.manager.LocalizedManager;
import unknow.kyhtanil.server.manager.UUIDManager;

public class ProjectileSystem extends IteratingSystem
	{
	private LocalizedManager locManager;
	private UUIDManager uuid;
	private ComponentMapper<Projectile> projectile;
	private ComponentMapper<PositionComp> position;

	public ProjectileSystem()
		{
		super(Aspect.all(Projectile.class, PositionComp.class));
		}

	protected void initialize()
		{
		}

	@Override
	protected void process(int e)
		{
		Projectile proj=projectile.get(e);
		PositionComp p=position.get(e);

		if(uuid.getEntity(proj.source)==null)
			{
			world.delete(e);
			return;
			}

		IntBag potential=locManager.get(p.x, p.y, 10, null); // TODO width & collision
		for(int i=0; i<potential.size(); i++)
			{
			int t=potential.get(i);
			if(p.distance(position.get(t))<2)
				{
				if(proj.onHit!=null)
					proj.onHit.run(t);
				world.delete(e);
				return;
				}
			}
		}
	}
