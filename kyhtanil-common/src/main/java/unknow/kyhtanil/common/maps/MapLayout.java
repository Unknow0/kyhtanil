package unknow.kyhtanil.common.maps;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MapLayout {
	private static final int size = 16;
	private static final int h = (int) (size * Math.sqrt(3) / 2);
	private Map<String, TilesetInfo> tileset;
	private List<MapEntry> maps;
	// private float tileWidth = 32, tileHeight = 32;
	private AtlasRegion t0;
	private AtlasRegion t1;

	public MapLayout() {
		maps = new ArrayList<MapEntry>();
		tileset = new HashMap<String, TilesetInfo>();
		t0 = new TextureAtlas(Gdx.files.internal("data/tex/texture.atlas")).findRegion("tileset/0");
		t1 = new TextureAtlas(Gdx.files.internal("data/tex/texture.atlas")).findRegion("tileset/1");
	}

	public MapLayout(DataInputStream in) throws IOException {
		int len = in.readInt();
		maps = new ArrayList<MapEntry>(len);
		for (int i = 0; i < len; i++)
			maps.add(new MapEntry(in));

		len = in.readInt();
		tileset = new HashMap<String, TilesetInfo>();
		for (int i = 0; i < len; i++) {
			TilesetInfo ti = new TilesetInfo(in);
			tileset.put(ti.name, ti);
		}
		t0 = new TextureAtlas(Gdx.files.internal("data/tex/texture.atlas")).findRegion("tileset/0");
		t1 = new TextureAtlas(Gdx.files.internal("data/tex/texture.atlas")).findRegion("tileset/1");
	}

	public void save(DataOutputStream out) throws IOException {
		out.writeInt(maps.size());
		for (MapEntry e : maps)
			e.write(out);

		// TODO save only used tileset
		out.writeInt(tileset.size());
		for (TilesetInfo t : tileset.values())
			t.save(out);
	}

	public MapEntry get(int x, int y) {
		for (MapEntry e : maps) {
			if (x >= e.x && y >= e.y && x < e.x + e.w && y < e.y + e.h)
				return e;
		}
		return null;
	}

	public List<MapEntry> get(int sx, int sy, int ex, int ey) {
		List<MapEntry> list = new ArrayList<MapEntry>(5);
		for (MapEntry e : maps) {
			if (sx < e.x + e.w && sy < e.y + e.h && ex > e.x && ey > e.y)
				list.add(e);
		}
		return list;
	}

	/**
	 * check wall in map coordinate
	 * 
	 * @param x
	 * @param y
	 * @return
	 * @throws IOException
	 */
	public boolean isWall(int x, int y) throws IOException {
		MapEntry e = get(x, y);
		TilesetInfo ti = tileset.get(e.tileset);
		return ti.isWall(e.map().get(x - e.x, y - e.y));
	}

	/**
	 * check wall in game coordinate
	 * 
	 * @param x
	 * @param y
	 * @param unitsPerPixel
	 * @return
	 * @throws IOException
	 */
	public boolean isWall(float x, float y) throws IOException {
		double my = y / (size * 1.5);
		double yr = my % 1;

		boolean shift = (int) my % 2 == 0;

		double mx = (x - (shift ? h : 0)) / 2. / h;
		double xr = mx % 1;

		if (yr < .3) { // we are in the pointy part
			if (xr < .5) {
				if (yr < -.6 * xr + .3) {
					my -= 1;
					if (!shift)
						mx -= 1;
				}
			} else if (yr < .6 * xr - .3) {
				my -= 1;
				if (shift)
					mx += 1;
			}
		}
		return isWall((int) mx, (int) my);
	}

	public void add(int x, int y, int w, int h, String name, String tilesetFile) {
		MapEntry e = new MapEntry(x, y, w, h, name, tilesetFile);
		e.map = new MapModel(w, h);
		maps.add(e);
	}

	public List<MapEntry> maps() {
		return maps;
	}

	public void draw(Batch batch, Viewport vp) throws IOException {
		float h2 = 2 * h;
		float s15 = size * 1.5f;

		Vector3 v = vp.getCamera().position;
		int sx = (int) ((v.x - vp.getWorldWidth() / 2) / h2) - 1;
		int sy = (int) ((v.y - vp.getWorldHeight() / 2) / s15) - 1;
		// if(sx<minX)
		// sx=minX;
		// if(sy<minY)
		// sy=minY;
		int ex = sx + 2 + (int) ((vp.getWorldWidth()) / h2);
		int ey = sy + 2 + (int) ((vp.getWorldHeight()) / s15);
		// if(ex>maxX)
		// ex=maxX;
		// if(ey>maxY)
		// ey=maxY;

		for (MapEntry e : get(sx, sy, ex, ey)) {
			MapModel m = e.map();
			TileSet tileset = tileset(e.tileset);
			float mxe = e.x + e.w;
			float mye = e.y + e.h;

			int x = e.x < sx ? sx : e.x;
			while (x <= ex && x < mxe) {
				int y = e.y < sy ? sy : e.y;
				while (y <= ey && y < mye) {
					// TextureRegion tex = tileset.get(m.get(x - e.x, y - e.y));

					int px = x * h * 2;
					if (y % 2 == 0)
						px += h;
					TextureRegion r = m.get(x - e.x, y - e.y) == 1 ? t1 : t0;
					batch.draw(r, px, y * size * 1.5f, r.getRegionWidth(), r.getRegionHeight());
					y++;
				}
				x++;
			}
		}
	}

	// public float tileWidth() {
	// return tileWidth;
	// }
	//
	// public float tileHeight() {
	// return tileHeight;
	// }

	public TileSet tileset(String name) throws IOException {
		TilesetInfo ti = tileset.get(name);
		if (ti == null)
			throw new IOException("no tileset found");
		return ti.tileset();
	}

	public Collection<TilesetInfo> tilesetInfo() {
		return tileset.values();
	}

	public void setTilesetInfo(Iterable<TilesetInfo> tilesetInfo) {
		tileset.clear();
		for (TilesetInfo t : tilesetInfo)
			tileset.put(t.name, t);
	}

	/**
	 * list tilesetName
	 */
	public String[] tilesets() {
		return tileset.keySet().toArray(new String[0]);
	}

	public static class TilesetInfo {
		public String name;
		public int width, height;
		private BitSet wall;
		private TileSet tileset = null;

		public TilesetInfo(String name, TileSet ts, int width, int height) {
			this.name = name;
			this.tileset = ts;
			this.width = width;
			this.height = height;
			wall = new BitSet();
		}

		public TilesetInfo(DataInputStream in) throws IOException {
			name = in.readUTF();
			width = in.readInt();
			height = in.readInt();
			int l = in.readInt();
			long[] v = new long[l];
			for (int i = 0; i < l; i++)
				v[i] = in.readLong();
			wall = BitSet.valueOf(v);
		}

		public void save(DataOutputStream out) throws IOException {
			out.writeUTF(name);
			out.writeInt(width);
			out.writeInt(height);
			long[] l = wall.toLongArray();
			out.writeInt(l.length);
			for (int i = 0; i < l.length; i++)
				out.writeLong(l[i]);
		}

		public TileSet tileset() throws IOException {
			if (tileset == null)
				tileset = new TileSet(Gdx.files.internal("data/tileset/" + name), width, height);
			return tileset;
		}

		public boolean isWall(int i) {
			return wall.get(i);
		}

		public void toggleWall(int id) {
			wall.flip(id);
		}

		@Override
		public String toString() {
			return name;
		}
	}

	public static class MapEntry {
		/** position in tile */
		public int x, y;
		/** size in tile */
		public int w, h;
		private String tileset;
		public String name;
		private MapModel map;

		public MapEntry(int x, int y, int w, int h, String name, String tileset) {
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
			this.name = name;
			this.tileset = tileset;
		}

		public MapEntry(DataInputStream in) throws IOException {
			x = in.readInt();
			y = in.readInt();
			w = in.readInt();
			h = in.readInt();
			name = in.readUTF();
			tileset = in.readUTF();
		}

		public void set(int x, int y, int w, int h, String name) {
			this.x = x;
			this.y = y;
			this.name = name;
			if (this.w != w || this.h != h) {
				try {
					MapModel m = map();
					m.setSize(w, h);
				} catch (Exception e) {
				}
			}
			this.w = w;
			this.h = h;
		}

		public void write(DataOutputStream out) throws IOException {
			out.writeInt(x);
			out.writeInt(y);
			out.writeInt(w);
			out.writeInt(h);
			out.writeUTF(name);
			out.writeUTF(tileset);
		}

		public MapModel map() throws IOException {
			if (map == null) {
				try {
					String s = "data/maps/" + name + ".map";
					DataInputStream in;
					if (Gdx.files != null) {
						FileHandle fh = Gdx.files.internal(s);
						in = new DataInputStream(fh.read());
					} else
						in = new DataInputStream(new FileInputStream(s));
					map = new MapModel(in);
					in.close();
				} catch (FileNotFoundException e) {
					map = new MapModel(w, h);
				}
			}
			return map;
		}

		public void save() throws IOException {
			if (map != null) {
				DataOutputStream out = new DataOutputStream(new FileOutputStream("data/maps/" + name + ".map"));
				map.save(out);
				out.close();
			}
		}

		public void setTileset(String name) {
			tileset = name;
		}

		public String tileset() {
			return tileset;
		}
	}
}
