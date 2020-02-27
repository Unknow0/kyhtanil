package unknow.kyhtanil.client.system;

import java.sql.Ref;
import java.util.HashMap;
import java.util.Map;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import unknow.kyhtanil.common.component.SpriteComp;

public class TexManager extends BaseEntitySystem {
	private static TextureAtlas atlas;

	private ComponentMapper<SpriteComp> sprite;

	private static Map<String, Ref> cache = new HashMap<>();

	public TexManager() {
		super(Aspect.all(SpriteComp.class));
		atlas = new TextureAtlas(Gdx.files.internal("data/tex/texture.atlas"));
	}

	@Override
	public void inserted(IntBag entities) {
		// String[] r = new String[entities.size()];
		// for (int i = 0; i < entities.size(); i++) {
		// String t = sprite.get(entities.get(i)).tex;
		// r[i] = t;
		// if (!cache.containsKey(t))
		// cache.put(t, new Ref(t));
		// }
		// Gdx.app.postRunnable(new Loader(r));
	}

	@Override
	public void removed(IntBag entities) {
		// for (int i = 0; i < entities.size(); i++) {
		// SpriteComp t = sprite.get(entities.get(i));
		// if (t == null)
		// continue;
		// Ref ref = cache.get(t.tex);
		// if (ref != null && ref.unref())
		// cache.remove(t.tex);
		// }
	}

	@Override
	protected void processSystem() {
	}

	public static Drawable get(String tex) {
		Array<AtlasRegion> regions = atlas.findRegions(tex);
		if (regions.size == 0)
			return null;
		AtlasRegion region = regions.get(0);
		if (region.splits != null) {
			int[] splits = region.splits;
			NineDrawable patch = new NineDrawable(region, splits[0], splits[1], splits[2], splits[3]);
			if (region.pads != null)
				patch.setPadding(region.pads[0], region.pads[1], region.pads[2], region.pads[3]);
			return patch;
		}
		if (region.index > -1) {
			// TODO animation
		}
		return new RegionDrawable(region);
	}

	// private static class Ref {
	// int count = 0;
	// String path;
	// TextureRegion tex;
	//
	// public Ref(String path) {
	// this.path = path;
	// }
	//
	// public void ref() {
	// if (count == 0) {
	// if (tex == null)
	// tex = atlas.
	// }
	// count++;
	// }
	//
	// public boolean unref() {
	// if (count-- <= 0 && tex != null)
	// tex.getTexture().dispose();
	// return count == 0;
	// }
	// }
	//
	// private class Loader implements Runnable {
	// private String[] t;
	//
	// public Loader(String[] t) {
	// this.t = t;
	// }
	//
	// public void run() {
	// for (int i = 0; i < t.length; i++) {
	// Ref ref = cache.get(t[i]);
	// if (ref != null)
	// ref.ref();
	// }
	// }
	// }

	public static interface Drawable {
		void draw(Batch batch, float x, float y, float width, float height);
	}

	public static class NineDrawable extends NinePatch implements Drawable {
		public NineDrawable(TextureRegion region, int left, int right, int top, int bottom) {
			super(region, left, right, top, bottom);
		}

	}

	public static class RegionDrawable implements Drawable {
		private TextureRegion region;

		public RegionDrawable(TextureRegion region) {
			this.region = region;
		}

		@Override
		public void draw(Batch batch, float x, float y, float width, float height) {
			batch.draw(region, x, y, width, height);
		}

		public int getWidth() {
			return region.getRegionWidth();
		}

		public int getHeight() {
			return region.getRegionHeight();
		}
	}
}
