package unknow.kyhtanil.client.graphics;

import unknow.kyhtanil.client.*;
import unknow.kyhtanil.common.*;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.viewport.*;

public class MobActor
	{
	protected UUID uuid;
	protected Texture tex;
	protected byte[] dir=new byte[2];
	protected float speed=1f;

	protected float x;
	protected float y;

	protected float w, h;

	protected float rotation=0;

	protected int hp;

	public MobActor(Texture tex, UUID uuid, int hp)
		{
		super();
		this.tex=tex;
		this.uuid=uuid;
		this.hp=hp;
		this.w=Main.pixelToUnit(tex.getWidth());
		this.h=Main.pixelToUnit(tex.getHeight());
		}

	public void draw(Batch batch, Viewport vp)
		{
		Matrix4 oldMatrix=null;
		if(rotation!=0)
			{
			batch.end();
			oldMatrix=batch.getTransformMatrix().cpy();
			Matrix4 m=batch.getTransformMatrix().translate(x, y, 0);
			m.rotateRad(0, 0, 1, rotation);
			batch.begin();
			batch.draw(tex, -w/2, -h/2, w, h);
			batch.end();
			batch.setTransformMatrix(oldMatrix);
			batch.begin();
			}
		else
			batch.draw(tex, x-w/2, y-h/2, w, h);
		}

	public UUID getUUID()
		{
		return uuid;
		}

	public void setDirX(int b)
		{
		dir[0]=(byte)(speed*b);
		}

	public void setDirY(int b)
		{
		dir[1]=(byte)(speed*b);
		}

	public void setPosition(float x, float y)
		{
		this.x=x;
		this.y=y;
		}

	public float getX()
		{
		return x;
		}

	public float getY()
		{
		return y;
		}

	public void setRotation(float rad)
		{
		rotation=rad;
		}

	public double distance(float x, float y)
		{
		return Math.sqrt((this.x-x)*(this.x-x)+(this.y-y)*(this.y-y));
		}

	public void update(Move move)
		{
//		ByteBuffer buf=move.getDir();
//		dir[0]=buf.get();
//		dir[1]=buf.get();
//		x=move.getX();
//		y=move.getY();
		}

	public void act(float delta)
		{
		setPosition(x+dir[0]*delta, y+dir[1]*delta);
		}
	}
