package unknow.kyhtanil.client.system;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

import unknow.kyhtanil.client.Main;
import unknow.kyhtanil.client.State;
import unknow.kyhtanil.client.component.TargetComp;
import unknow.kyhtanil.common.component.PositionComp;
import unknow.kyhtanil.common.component.SpriteComp;
import unknow.kyhtanil.common.component.StatShared;
import unknow.kyhtanil.common.maps.MapLayout;

public class RenderingSystem extends IteratingSystem
	{
	private SpriteBatch batch;
	private Viewport vp;
	protected MapLayout layout;

	private ComponentMapper<StatShared> info;
	private ComponentMapper<TargetComp> target;
	private ComponentMapper<PositionComp> position;
	private ComponentMapper<SpriteComp> sprite;

	private Texture targetTex;
	private Vector2 targetSize;
	private Texture hpTex;

	public RenderingSystem(Viewport vp) throws FileNotFoundException, IOException
		{
		super(Aspect.all(PositionComp.class, SpriteComp.class, StatShared.class));
		this.vp=vp;
		this.layout=new MapLayout(new DataInputStream(new FileInputStream("data/maps.layout")));
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
	protected boolean checkProcessing()
		{
		return State.entity>=0;
		}

	@Override
	protected void begin()
		{
		PositionComp p=position.get(State.entity);
		float x=p.x;
		float y=p.y;
		if(x<vp.getWorldWidth()/2)
			x=vp.getWorldWidth()/2;
		if(y<vp.getWorldHeight()/2)
			y=vp.getWorldHeight()/2;
		vp.getCamera().position.set(x, y, 0);
		vp.getCamera().update();

		batch.setProjectionMatrix(vp.getCamera().combined);
		batch.begin();
		try
			{
			layout.draw(batch, Main.pixelToUnit(1), vp);
			}
		catch (IOException e)
			{
			e.printStackTrace();
			}
		}

	@Override
	protected void process(int id)
		{
		PositionComp pos=position.get(id);
		SpriteComp s=sprite.get(id);
		TextureRegion tex=TexManager.get(s.tex);
		if(tex!=null)
			{
			if(s.rotation!=0)
				{
				batch.end();
				Matrix4 cpy=batch.getTransformMatrix().cpy();
				batch.getTransformMatrix().translate(pos.x, pos.y, 0);
				batch.getTransformMatrix().rotateRad(0, 0, 1, s.rotation);

				batch.begin();
				batch.draw(tex, -s.w/2, -s.h/2, s.w, s.h);
				batch.end();

				batch.setTransformMatrix(cpy);
				batch.begin();
				}
			else
				batch.draw(tex, pos.x-s.w/2, pos.y-s.h/2, s.w, s.h);
			}
		if(target.has(id))
			batch.draw(targetTex, pos.x-targetSize.x/2, pos.y-targetSize.y/2, targetSize.x, targetSize.y);

		StatShared c=info.get(id);

		batch.draw(hpTex, pos.x-s.w/2, pos.y+s.h/2+.5f, s.w*c.hp*1f/c.maxHp, .5f);
		}

	@Override
	protected void end()
		{
		batch.end();
		}
	}
