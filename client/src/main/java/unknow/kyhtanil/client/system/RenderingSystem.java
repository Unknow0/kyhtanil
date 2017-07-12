package unknow.kyhtanil.client.system;

import unknow.kyhtanil.client.*;
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
	private ComponentMapper<TargetComp> target;
	private ComponentMapper<PositionComp> position;
	private ComponentMapper<SpriteComp> sprite;

	private Texture targetTex;
	private Vector2 targetSize;
	private Texture hpTex;

	public RenderingSystem(Camera cam)
		{
		super(Aspect.all(PositionComp.class, SpriteComp.class, CalculatedComp.class));
		this.cam=cam;
		targetTex=new Texture(Gdx.files.internal("data/tex/target.png"));
		targetSize=new Vector2(Main.pixelToUnit(targetTex.getWidth()), Main.pixelToUnit(targetTex.getHeight()));
		hpTex=new Texture(Gdx.files.internal("data/tex/hp.png"));
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
		PositionComp pos=position.get(id);
		SpriteComp s=sprite.get(id);

		if(s.rotation!=0)
			{
			batch.end();
			Matrix4 cpy=batch.getTransformMatrix().cpy();
			batch.getTransformMatrix().translate(pos.x, pos.y, 0);
			batch.getTransformMatrix().rotateRad(0, 0, 1, s.rotation);

			batch.begin();
			batch.draw(s.tex, -s.w/2, -s.h/2, s.w, s.h);
			batch.end();

			batch.setTransformMatrix(cpy);
			batch.begin();
			}
		else
			batch.draw(s.tex, pos.x-s.w/2, pos.y-s.h/2, s.w, s.h);
		if(target.has(id))
			batch.draw(targetTex, pos.x-targetSize.x/2, pos.y-targetSize.y/2, targetSize.x, targetSize.y);

		CalculatedComp c=calculated.get(id);

		batch.draw(hpTex, pos.x-s.w/2, pos.y+s.h/2+.5f, s.w*c.hp*1f/c.maxHp, .5f);
		}

	@Override
	protected void end()
		{
		batch.end();
		}
	}
