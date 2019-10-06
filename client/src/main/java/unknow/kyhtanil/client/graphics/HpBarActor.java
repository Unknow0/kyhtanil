package unknow.kyhtanil.client.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import unknow.kyhtanil.client.State;
import unknow.kyhtanil.common.component.CalculatedComp;

public class HpBarActor extends Actor
	{
	private final Texture tex;

	public HpBarActor()
		{
		tex=new Texture(Gdx.files.internal("data/tex/hp.png"));
		}

	@Override
	public void draw(Batch batch, float parentAlpha)
		{
		CalculatedComp total=State.stat;
		float r=(float)(total.hp*1./total.maxHp);
		batch.draw(tex, getX(), getY(), getWidth(), getHeight()*r);
		}
	}
