package unknow.kyhtanil.client.system;

import unknow.kyhtanil.client.*;
import unknow.kyhtanil.client.artemis.*;
import unknow.kyhtanil.client.component.*;
import unknow.kyhtanil.common.component.*;

import com.artemis.*;
import com.artemis.systems.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;

public class RenderingSystem extends IteratingSystem
	{
	private SpriteBatch batch;
	private Camera cam;

	private ComponentMapper<CalculatedComp> calculated;

	private Texture targetTex;
	private Vector2 targetSize;
	private Texture hpTex;

	public RenderingSystem(Camera cam)
		{
		super(Aspect.all(PositionComp.class, SpriteComp.class, CalculatedComp.class));
		this.cam=cam;
		targetTex=new Texture(Gdx.files.internal("target.png"));
		targetSize=new Vector2(Main.pixelToUnit(targetTex.getWidth()), Main.pixelToUnit(targetTex.getHeight()));
		hpTex=new Texture(Gdx.files.internal("hp.png"));
		}

	@Override
	protected void initialize()
		{
		super.initialize();
		batch=new SpriteBatch();
		}

	@Override
	protected void begin()
		{
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		}

	@Override
	protected void process(int id)
		{
		PositionComp pos=Builder.getPosition(id);
		SpriteComp sprite=Builder.getSprite(id);

		if(sprite.rotation!=0)
			{
			batch.end();
			Matrix4 cpy=batch.getTransformMatrix().cpy();
			batch.getTransformMatrix().translate(pos.x, pos.y, 0);
			batch.getTransformMatrix().rotateRad(0, 0, 1, sprite.rotation);

			batch.begin();
			batch.draw(sprite.tex, -sprite.w/2, -sprite.h/2, sprite.w, sprite.h);
			batch.end();

			batch.setTransformMatrix(cpy);
			batch.begin();
			}
		else
			batch.draw(sprite.tex, pos.x-sprite.w/2, pos.y-sprite.h/2, sprite.w, sprite.h);
		if(Builder.isTarget(id))
			batch.draw(targetTex, pos.x-targetSize.x/2, pos.y-targetSize.y/2, targetSize.x, targetSize.y);

		CalculatedComp c=calculated.get(id);

		batch.draw(hpTex, pos.x-sprite.w/2, pos.y+sprite.h/2+.5f, sprite.w*c.hp*1f/c.maxHp, .5f);
		}

	@Override
	protected void end()
		{
		batch.end();
		}
	}
