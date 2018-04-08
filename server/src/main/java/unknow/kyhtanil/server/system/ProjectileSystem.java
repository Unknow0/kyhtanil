package unknow.kyhtanil.server.system;

import unknow.kyhtanil.common.component.*;
import unknow.kyhtanil.server.component.*;
import unknow.kyhtanil.server.manager.*;

import com.artemis.*;
import com.artemis.systems.*;
import com.artemis.utils.*;

public class ProjectileSystem extends IteratingSystem
	{
	private LocalizedManager locManager;
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

		IntBag potential=locManager.get(p.x, p.y, 10); // TODO width
		for(int i=0; i<potential.size(); i++)
			{
			int t=potential.get(i);
			if(p.hit(position.get(t)))
				{
				if(proj.onHit!=null)
					proj.onHit.run(t);
				world.delete(e);
				return;
				}
			}
		}
	}
