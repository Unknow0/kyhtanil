package unknow.kyhtanil.common;

import java.util.Map;
import java.util.WeakHashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Texture manager
 * 
 * @author unknow
 */
public class TexManager {
	private static final Drawable NULL_DRAWABLE = new Drawable() {
		@Override
		public void draw(Batch batch, float x, float y, float width, float height) {
		}
	};
	private static Graphics graphics;
	private static TextureAtlas atlas;

	private static Map<String, Drawable> cache = new WeakHashMap<>();

	/**
	 * initialize the Manager, (load the texture atlas)
	 */
	public static final void init() {
		graphics = Gdx.graphics;
		atlas = new TextureAtlas(Gdx.files.internal("data/tex/texture.atlas"));
	}

	private static Drawable create(String tex) {
		Array<AtlasRegion> regions = atlas.findRegions(tex);
		if (regions.size == 0)
			return NULL_DRAWABLE;
		AtlasRegion region = regions.get(0);
		if (region.splits != null) {
			int[] splits = region.splits;
			NineDrawable patch = new NineDrawable(region, splits[0], splits[1], splits[2], splits[3]);
			if (region.pads != null)
				patch.setPadding(region.pads[0], region.pads[1], region.pads[2], region.pads[3]);
			return patch;
		}
		if (region.index > -1) {
			return new AnimationDrawable(regions);
		}
		return new RegionDrawable(region);
	}

	/**
	 * @param tex the texture to load
	 * @return the drawable
	 */
	public static Drawable get(String tex) {
		Drawable drawable = cache.get(tex);
		if (drawable == null)
			cache.put(tex, drawable = create(tex));
		return drawable == NULL_DRAWABLE ? null : drawable;
	}

	/**
	 * a drawable
	 * 
	 * @author unknow
	 */
	public static interface Drawable {
		/**
		 * draw
		 * 
		 * @param batch  where
		 * @param x      position
		 * @param y      position
		 * @param width
		 * @param height
		 */
		void draw(Batch batch, float x, float y, float width, float height);
	}

	private static class NineDrawable extends NinePatch implements Drawable {
		private NineDrawable(TextureRegion region, int left, int right, int top, int bottom) {
			super(region, left, right, top, bottom);
		}
	}

	private static class RegionDrawable implements Drawable {
		private TextureRegion region;

		private RegionDrawable(TextureRegion region) {
			this.region = region;
		}

		@Override
		public void draw(Batch batch, float x, float y, float width, float height) {
			batch.draw(region, x, y, width, height);
		}
	}

	private static class AnimationDrawable implements Drawable {
		private Animation<TextureRegion> a;
		private float t;

		public AnimationDrawable(Array<AtlasRegion> regions) {
			this.a = new Animation<>(1f, regions, Animation.PlayMode.LOOP);
			this.t = 0;
		}

		@Override
		public void draw(Batch batch, float x, float y, float width, float height) {
			t += graphics.getDeltaTime();
			batch.draw(a.getKeyFrame(t), x, y, width, height);
		}
	}
}
