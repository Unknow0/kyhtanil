package unknow.kyhtanil.client.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import unknow.kyhtanil.client.State;
import unknow.kyhtanil.common.component.StatShared;

public abstract class BarActor extends Actor
	{
	private final NinePatch nine;
	private final Texture back;

	public BarActor(int color)
		{
		nine=new NinePatch(new Texture(Gdx.files.internal("data/tex/bar.png")), 1, 1, 0, 0);
		Pixmap pixmap=new Pixmap(1, 1, Format.RGBA8888);
		pixmap.setColor(color);
		pixmap.fill();
		back=new Texture(pixmap);
		}

	protected abstract float rate();

	@Override
	public void draw(Batch batch, float parentAlpha)
		{
		float r=rate();

		batch.draw(back, getX(), getY(), getWidth(), getHeight());
		nine.setLeftWidth(getWidth()*r-nine.getMiddleWidth()/2);
		nine.setRightWidth(getWidth()-nine.getLeftWidth()-nine.getMiddleWidth()/2);
		nine.draw(batch, getX(), getY(), getWidth(), getHeight());
		}

	public static class Hp extends BarActor
		{
		public Hp()
			{
			super(0xff0000ff);
			}

		protected float rate()
			{
			StatShared total=State.stat;
			return (float)(total.hp*1./total.maxHp);
			}
		}

	public static class Mp extends BarActor
		{
		public Mp()
			{
			super(0x0000ffff);
			}

		protected float rate()
			{
			StatShared total=State.stat;
			return (float)(total.mp*1./total.maxMp);
			}
		}
	}
