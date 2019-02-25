package unknow.kyhtanil.client.system;

import java.io.*;

import org.slf4j.*;

import unknow.kyhtanil.client.*;
import unknow.kyhtanil.client.artemis.*;
import unknow.kyhtanil.client.component.*;
import unknow.kyhtanil.client.screen.*;
import unknow.kyhtanil.common.component.*;
import unknow.kyhtanil.common.pojo.*;

import com.artemis.*;
import com.artemis.utils.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.viewport.*;

public class InputSystem extends BaseSystem implements InputProcessor
	{
	private static final Logger log=LoggerFactory.getLogger(InputSystem.class);
	private Viewport vp;

	public int up=Input.Keys.W;
	public int down=Input.Keys.S;
	public int left=Input.Keys.A;
	public int right=Input.Keys.D;

	public int showStat=Input.Keys.C;

	public int[] bar=new int[] {Input.Keys.NUM_1, Input.Keys.NUM_2, Input.Keys.NUM_3, Input.Keys.NUM_4, Input.Keys.NUM_5, Input.Keys.NUM_6, Input.Keys.NUM_7, Input.Keys.NUM_8, Input.Keys.NUM_9};

	protected EntitySubscription allPosition;

	private ComponentMapper<CalculatedComp> calculated;
	private UUIDManager manager;
	private WorldScreen screen;

	private long lastSend=0;
	private float lastX;
	private float lastY;
	private double dirX=0;
	private double dirY=0;

	public InputSystem(Viewport vp, WorldScreen screen, UUIDManager manager)
		{
		this.vp=vp;
		this.screen=screen;
		this.manager=manager;
		}

	protected void initialize()
		{
		AspectSubscriptionManager sm=world.getAspectSubscriptionManager();
		allPosition=sm.get(Aspect.all(PositionComp.class));
		}

	public boolean keyDown(int keycode)
		{
		VelocityComp v=Builder.getVelocity(State.entity);
		CalculatedComp c=calculated.get(State.entity);

		if(up==keycode||down==keycode||left==keycode||right==keycode)
			{// TODO take pj speed
			if(up==keycode)
				dirY=1.;
			else if(down==keycode)
				dirY=-1.;
			else if(left==keycode)
				dirX=-1.;
			else if(right==keycode)
				dirX=1;

			v.speed=c.moveSpeed;
			v.direction=(float)Math.atan2(dirY, dirX);
			}
		return true;
		}

	public boolean keyUp(int keycode)
		{
		VelocityComp v=Builder.getVelocity(State.entity);
		if(up==keycode||down==keycode||left==keycode||right==keycode)
			{
			if(up==keycode||down==keycode)
				dirY=0.;
			else if(left==keycode||right==keycode)
				dirX=0.;
			if(dirY==0.&&dirX==0.)
				v.speed=0f;
			v.direction=(float)Math.atan2(dirY, dirX);
			}
		else if(showStat==keycode)
			screen.toggleStat();
		else if(Input.Keys.ESCAPE==keycode)
			screen.closeLast();
		else
			{
			for(int i=0; i<bar.length; i++)
				{
				if(bar[i]==keycode)
					{
					try
						{
						Vector2 vec=vp.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
						IntBag targets=Builder.getTarget();
						UUID uuid=null;
						if(!targets.isEmpty())
							{
							int t=targets.get(0);
							uuid=manager.getUuid(t);
							log.info("attaque {} {}", t, uuid);
							}
						Main.co().attack(State.uuid, i, uuid, vec.x, vec.y);
						}
					catch (IOException e)
						{
						// TODO Auto-generated catch block
						e.printStackTrace();
						}
					break;
					}
				}
			}
		return true;
		}

	public boolean keyTyped(char character)
		{
		return false;
		}

	public boolean touchDown(int screenX, int screenY, int pointer, int button)
		{
		Vector2 v=vp.unproject(new Vector2(screenX, screenY));
		IntBag targets=Builder.getTarget();
		for(int i=0; i<targets.size(); i++)
			{
			int e=targets.get(i);
			EntityEdit edit=world.getEntity(e).edit();
			edit.remove(TargetComp.class);
			}

		IntBag entities=allPosition.getEntities();

		for(int i=0; i<entities.size(); i++)
			{
			int e=entities.get(i);
			PositionComp p=Builder.getPosition(e);
			if(p.distance(v.x, v.y)<1)
				{
				log.info("target {} ({}, {})", manager.getUuid(e), p.x, p.y);
				EntityEdit edit=world.getEntity(e).edit();
				edit.create(TargetComp.class);
				break;
				}
			}
		return true;
		}

	public boolean touchUp(int screenX, int screenY, int pointer, int button)
		{
		return false;
		}

	public boolean touchDragged(int screenX, int screenY, int pointer)
		{
		return false;
		}

	public boolean mouseMoved(int screenX, int screenY)
		{
		return false;
		}

	public boolean scrolled(int amount)
		{
		return false;
		}

	@Override
	protected void processSystem()
		{
		// not char logged => nothing to do
		if(State.stat==null||State.uuid==null)
			return;
		SpriteComp s=Builder.getSprite(State.entity);
		PositionComp p=Builder.getPosition(State.entity);
		Vector2 d=vp.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
		IntBag target=Builder.getTarget();
		if(!target.isEmpty())
			{
			PositionComp t=Builder.getPosition(target.get(0));
			d.set(t.x, t.y);
			}
		s.rotation=(float)Math.atan2(d.y-p.y, d.x-p.x);

		long now=System.currentTimeMillis();
		if(now-lastSend>250&&(lastX!=p.x||lastY!=p.y))
			{
			VelocityComp v=Builder.getVelocity(State.entity);
			try
				{
				Main.co().update(State.uuid, p.x, p.y, v.direction);
				}
			catch (IOException e)
				{
				// TODO Auto-generated catch block
				e.printStackTrace();
				}
			lastX=p.x;
			lastY=p.y;
			lastSend=now;
			}
		}
	}
