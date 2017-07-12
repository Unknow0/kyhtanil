package unknow.kyhtanil.server.manager;

import unknow.common.data.*;
import unknow.kyhtanil.common.component.*;
import unknow.kyhtanil.server.component.*;
import unknow.kyhtanil.server.component.StateComp.*;

import com.artemis.*;
import com.artemis.utils.*;

public class LocalizedManager extends BaseEntitySystem
	{
	private BTree<Loc,IntBag> locMap=new BTree<Loc,IntBag>(50);
	private BTree<Integer,Loc> objects=new BTree<Integer,Loc>(50);

	private float w, h;

	private ComponentMapper<PositionComp> position;
	private ComponentMapper<StateComp> state;

	/**
	 * create a manager with a w x h square to store entity
	 */
	public LocalizedManager(float w, float h)
		{
		super(Aspect.all(PositionComp.class));
		this.w=w;
		this.h=h;
		}

	@Override
	protected void initialize()
		{
//		position=ComponentMapper.getFor(PositionComp.class, world);
		}

	@Override
	public void inserted(int entityId)
		{
		PositionComp p=position.getSafe(entityId);
		if(p==null)
			return;
		// don't add pj at there creation
		StateComp s=state.getSafe(entityId);
		if(s!=null&&s.state!=States.IN_GAME)
			return;

		Loc loc=new Loc(p.x-p.x%w, p.y-p.y%h);
		IntBag bag=locMap.get(loc);
		if(bag==null)
			{
			bag=new IntBag();
			locMap.put(loc, bag);
			}
		bag.add(entityId);
		objects.put(entityId, loc);
		}

	public void changed(int entityId)
		{
		Loc loc=objects.get(entityId);
		IntBag bag=locMap.get(loc);

		PositionComp p=position.getSafe(entityId);

		if(p==null)
			{
			bag.remove(entityId);
			objects.remove(entityId);
			return;
			}

		Loc nloc=new Loc(p.x-p.x%w, p.y-p.y%h);
		if(nloc.x==loc.x&&nloc.y==loc.y)
			return;

		bag.remove(entityId);

		bag=locMap.get(nloc);
		if(bag==null)
			{
			bag=new IntBag();
			locMap.put(nloc, bag);
			}
		bag.add(entityId);
		objects.put(entityId, nloc);
		}

	@Override
	public void removed(int entityId)
		{
		Loc loc=objects.remove(entityId);
		if(loc==null)
			return;
		IntBag bag=locMap.get(loc);
		bag.remove(entityId);
		}

	public IntBag get(float x, float y, float r, ComponentMapper<?>... expected)
		{
		float mx=x+2*r;
		float my=y+2*r;
		IntBag bag=new IntBag();
		for(float cx=(x-r-(x-r)%w); cx<=mx; cx+=w)
			{
			for(float cy=y-r-(y-r)%h; cy<=my; cy+=h)
				{
				IntBag l=locMap.get(new Loc(cx, cy));
				if(l!=null)
					{
					loop: for(int i=0; i<l.size(); i++)
						{
						int e=l.get(i);
						PositionComp p=position.getSafe(e);
						if(p==null)
							continue;
						for(int j=0; j<expected.length; j++)
							{
							if(!expected[j].has(e))
								continue loop;
							}

						float dx=x-p.x;
						float dy=y-p.y;
						double dist=Math.sqrt(dx*dx+dy*dy);
						if(dist<=r)
							bag.add(e);
						}
					}
				}
			}
		return bag;
		}

	private static final class Loc implements Comparable<Loc>
		{
		private float x, y;

		public Loc(float x, float y)
			{
			this.x=x;
			this.y=y;
			}

		public int compareTo(Loc l)
			{
			float cmp=l.x-x;
			if(cmp==0)
				cmp=l.y-y;
			return (int)cmp;
			}
		}

	protected void processSystem()
		{
		}

//	public static final LocalizedManager self()
//		{
//		return self;
//		}

//	public static final IntBag get(float x, float y, float r, ComponentMapper<?>... expected)
//		{
//		return self._get(x, y, r, expected);
//		}
//
//	public static final void changed(int entityId)
//		{
//		self._changed(entityId);
//		}
	}
