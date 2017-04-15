package unknow.game.admin.system;

import unknow.kyhtanil.client.*;
import unknow.kyhtanil.client.artemis.*;
import unknow.kyhtanil.common.component.*;

import com.badlogic.gdx.*;

public class InputSystem implements InputProcessor
	{
	private double dirX=0;
	private double dirY=0;

	public boolean keyDown(int keycode)
		{
		VelocityComp v=Builder.getVelocity(State.entity);
		if(Input.Keys.UP==keycode||Input.Keys.DOWN==keycode||Input.Keys.LEFT==keycode||Input.Keys.RIGHT==keycode)
			{// TODO take pj speed
			if(Input.Keys.UP==keycode)
				dirY=1.;
			else if(Input.Keys.DOWN==keycode)
				dirY=-1.;
			else if(Input.Keys.LEFT==keycode)
				dirX=-1.;
			else if(Input.Keys.RIGHT==keycode)
				dirX=1;

			v.speed=2f;
			v.direction=(float)Math.atan2(dirY, dirX);
			}
		return true;
		}

	public boolean keyUp(int keycode)
		{
		VelocityComp v=Builder.getVelocity(State.entity);
		if(Input.Keys.UP==keycode||Input.Keys.DOWN==keycode||Input.Keys.LEFT==keycode||Input.Keys.RIGHT==keycode)
			{
			if(Input.Keys.UP==keycode||Input.Keys.DOWN==keycode)
				dirY=0.;
			else if(Input.Keys.LEFT==keycode||Input.Keys.RIGHT==keycode)
				dirX=0.;
			if(dirY==0.&&dirX==0.)
				v.speed=0f;
			v.direction=(float)Math.atan2(dirY, dirX);
			}
		return true;
		}

	public boolean keyTyped(char character)
		{
		return false;
		}

	public boolean touchDown(int screenX, int screenY, int pointer, int button)
		{
		return false;
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
	}
