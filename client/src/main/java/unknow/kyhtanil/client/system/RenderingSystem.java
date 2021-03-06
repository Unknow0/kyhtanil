package unknow.kyhtanil.client.system;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.viewport.Viewport;

import unknow.kyhtanil.client.component.TargetComp;
import unknow.kyhtanil.common.TexManager;
import unknow.kyhtanil.common.TexManager.Drawable;
import unknow.kyhtanil.common.component.Position;
import unknow.kyhtanil.common.component.Sprite;
import unknow.kyhtanil.common.component.StatShared;
import unknow.kyhtanil.common.maps.MapLayout;

/**
 * Render sprite
 * 
 * @author unknow
 */
public class RenderingSystem extends IteratingSystem {
	private SpriteBatch batch;
	private Viewport vp;
	protected MapLayout layout;

	private ComponentMapper<StatShared> info;
	private ComponentMapper<TargetComp> target;
	private ComponentMapper<Position> position;
	private ComponentMapper<Sprite> sprite;

	private Drawable targetTex;
	private Texture hpTex;

	private State state;

	/**
	 * create new RenderingSystem
	 * 
	 * @param vp the viewport to use
	 * @throws IOException if we failed to load maps layout
	 */
	public RenderingSystem(Viewport vp) throws IOException {
		super(Aspect.all(Position.class, Sprite.class, StatShared.class));
		this.vp = vp;
		this.layout = new MapLayout(new DataInputStream(new FileInputStream("data/maps.layout")));
		targetTex = TexManager.get("target");
		Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
		pixmap.setColor(Color.RED);
		pixmap.fill();
		hpTex = new Texture(pixmap);
	}

	@Override
	protected void initialize() {
		super.initialize();
		batch = new SpriteBatch();
	}

	@Override
	protected boolean checkProcessing() {
		return state.entity >= 0 && sprite.has(state.entity);
	}

	@Override
	protected void begin() {
		Position p = position.get(state.entity);
		float x = p.x;
		float y = p.y;
		if (x < vp.getWorldWidth() / 2)
			x = vp.getWorldWidth() / 2;
		if (y < vp.getWorldHeight() / 2)
			y = vp.getWorldHeight() / 2;
		vp.getCamera().position.set(x, y, 0);
		vp.getCamera().update();

		batch.setProjectionMatrix(vp.getCamera().combined);
		batch.begin();
		try {
			layout.draw(batch, vp);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void process(int id) {
		Position pos = position.get(id);
		Sprite s = sprite.get(id);
		Drawable tex = TexManager.get(s.tex);
		if (tex != null) {
			if (s.rotation != 0) {
				batch.end();
				Matrix4 cpy = batch.getTransformMatrix().cpy();
				batch.getTransformMatrix().translate(pos.x, pos.y, 0);
				batch.getTransformMatrix().rotateRad(0, 0, 1, s.rotation);

				batch.begin();
				tex.draw(batch, -s.w / 2, -s.h / 2, s.w, s.h);
				batch.end();

				batch.setTransformMatrix(cpy);
				batch.begin();
			} else
				tex.draw(batch, pos.x - s.w / 2, pos.y - s.h / 2, s.w, s.h);
		}
		if (target.has(id))
			targetTex.draw(batch, pos.x - s.w / 2 - 5, pos.y - s.h / 2 - 5, s.w + 10, s.h + 10);

		StatShared c = info.get(id);

		batch.draw(hpTex, pos.x - s.w / 2, pos.y + s.h / 2 + .5f, s.w * c.hp * 1f / c.maxHp, .5f);
	}

	@Override
	protected void end() {
		batch.end();
	}
}
