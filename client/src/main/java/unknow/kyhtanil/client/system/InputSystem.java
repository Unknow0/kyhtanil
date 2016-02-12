package unknow.kyhtanil.client.system;

import java.io.*;

import org.apache.logging.log4j.*;

import unknow.kyhtanil.client.*;
import unknow.kyhtanil.client.artemis.*;
import unknow.kyhtanil.client.component.*;
import unknow.kyhtanil.client.screen.*;
import unknow.kyhtanil.common.*;

import com.artemis.*;
import com.artemis.utils.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.viewport.*;

public class InputSystem extends BaseSystem implements InputProcessor
	{
	private static final Logger log=LogManager.getFormatterLogger(InputSystem.class);
	private Viewport vp;

	public int up=Input.Keys.W;
	public int down=Input.Keys.S;
	public int left=Input.Keys.A;
	public int right=Input.Keys.D;

	public int showStat=Input.Keys.C;

	public int n1=Input.Keys.NUM_1;

	protected EntitySubscription allPosition;
	private UUIDManager manager;

	private WorldScreen screen;

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
		if(up==keycode)
			v.dirY=v.speed;
		else if(down==keycode)
			v.dirY=-v.speed;
		else if(left==keycode)
			v.dirX=-v.speed;
		else if(right==keycode)
			v.dirX=v.speed;

		return true;
		}

	public boolean keyUp(int keycode)
		{
		VelocityComp v=Builder.getVelocity(State.entity);
		if(up==keycode||down==keycode)
			v.dirY=0;
		else if(left==keycode||right==keycode)
			v.dirX=0;
		else if(n1==keycode)
			{
			try
				{
				Vector2 vec=vp.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
				IntBag targets=Builder.getTarget();
				UUID uuid=null;
				if(!targets.isEmpty())
					{
					int i=targets.get(0);
					uuid=manager.getUuid(i);
					log.info("attaque %d %s", i, uuid);
					}
				Main.co().attack(State.uuid, 0, targets.isEmpty()?null:uuid, vec.x, vec.y);
				}
			catch (IOException e)
				{
				// TODO Auto-generated catch block
				e.printStackTrace();
				}
			}
		else if(showStat==keycode)
			screen.showStat();
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
				log.info("target %d (%.2f, %.2f)", e, p.x, p.y);
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

	long lastSend=0;
	float lastX;
	float lastY;

	@Override
	protected void processSystem()
		{
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
		if(now-lastSend>250)
			{
			try
				{
				Main.co().update(State.uuid, p.x, p.y, 0, 0);
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
