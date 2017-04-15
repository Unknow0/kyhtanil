package unknow.kyhtanil.common.maps;

import java.io.*;
import java.util.*;

import com.badlogic.gdx.*;
import com.badlogic.gdx.files.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.viewport.*;

public class MapLayout
	{
	private Map<String,TilesetInfo> tileset;
	private List<MapEntry> maps;
	private float tileWidth=32, tileHeight=32;

	public MapLayout()
		{
		maps=new ArrayList<MapEntry>();
		tileset=new HashMap<String,TilesetInfo>();
		}

	public MapLayout(DataInputStream in) throws IOException
		{
		int len=in.readInt();
		maps=new ArrayList<MapEntry>(len);
		for(int i=0; i<len; i++)
			maps.add(new MapEntry(in));

		len=in.readInt();
		tileset=new HashMap<String,TilesetInfo>();
		for(int i=0; i<len; i++)
			{
			TilesetInfo ti=new TilesetInfo(in);
			tileset.put(ti.name, ti);
			}
		}

	public void save(DataOutputStream out) throws IOException
		{
		out.writeInt(maps.size());
		for(MapEntry e:maps)
			e.write(out);

		// TODO save only used tileset
		out.writeInt(tileset.size());
		for(TilesetInfo t:tileset.values())
			t.save(out);
		}

	public MapEntry get(float x, float y)
		{
		for(MapEntry e:maps)
			{
			if(x>e.x&&y>e.y&&x<e.x+e.w&&y<e.y+e.h)
				return e;
			}
		return null;
		}

	public List<MapEntry> get(int sx, int sy, int ex, int ey)
		{
		List<MapEntry> list=new ArrayList<MapEntry>(5);
		for(MapEntry e:maps)
			{
			if(sx<e.x+e.w&&sy<e.y+e.h&&ex>e.x&&ey>e.y)
				list.add(e);
			}
		return list;
		}

	public void add(int x, int y, int w, int h, String file, String tilesetFile)
		{
		MapEntry e=new MapEntry(x, y, w, h, file, tilesetFile);
		e.map=new MapModel(w, h);
		maps.add(e);

		}

	public List<MapEntry> maps()
		{
		return maps;
		}

	public void draw(Batch batch, float unitsPerPixel, Viewport vp) throws IOException
		{
		draw(batch, unitsPerPixel, vp, 0);
		}

	public void draw(Batch batch, float unitsPerPixel, Viewport vp, float gap) throws IOException
		{
		float tw=tileWidth*unitsPerPixel;
		float th=tileHeight*unitsPerPixel;

		Vector3 v=vp.getCamera().position;
		int sx=(int)((v.x-vp.getWorldWidth()/2)/tw);
		int sy=(int)((v.y-vp.getWorldHeight()/2)/th);
//		if(sx<minX)
//			sx=minX;
//		if(sy<minY)
//			sy=minY;
		int ex=sx+1+(int)((vp.getWorldWidth())/tw);
		int ey=sy+1+(int)((vp.getWorldHeight())/th);
//		if(ex>maxX)
//			ex=maxX;
//		if(ey>maxY)
//			ey=maxY;

		for(MapEntry e:get(sx, sy, ex, ey))
			{
			MapModel m=e.map();
			TileSet tileset=tileset(e.tileset);
			float mxe=e.x+e.w;
			float mye=e.y+e.h;

			int x=e.x<sx?sx:e.x;
			while (x<ex&&x<mxe)
				{
				int y=e.y<ey?sy:e.y;
				while (y<ey&&y<mye)
					{
					TextureRegion tex=tileset.get(m.get(x-e.x, y-e.y));
					batch.draw(tex, x*tw, y*th, tw, th);
					y++;
					}
				x++;
				}
			}
		}

	public float tileWidth()
		{
		return tileWidth;
		}

	public float tileHeight()
		{
		return tileHeight;
		}

	public TileSet tileset(String name) throws IOException
		{
		TilesetInfo ti=tileset.get(name);
		if(ti==null)
			throw new IOException("no tileset found");
		return ti.tileset();
		}

	public Collection<TilesetInfo> tilesetInfo()
		{
		return tileset.values();
		}

	public void setTilesetInfo(Iterable<TilesetInfo> tilesetInfo)
		{
		tileset.clear();
		for(TilesetInfo t:tilesetInfo)
			tileset.put(t.name, t);
		}

	/**
	 * list tilesetName
	 */
	public String[] tilesets()
		{
		return tileset.keySet().toArray(new String[0]);
		}

	public static class TilesetInfo
		{
		public String name;
		public int width, height;
		private BitSet wall;
		private TileSet tileset=null;

		public TilesetInfo(String name, TileSet ts, int width, int height)
			{
			this.name=name;
			this.tileset=ts;
			this.width=width;
			this.height=height;
			wall=new BitSet();
			}

		public TilesetInfo(DataInputStream in) throws IOException
			{
			name=in.readUTF();
			width=in.readInt();
			height=in.readInt();
			int l=in.readInt();
			long[] v=new long[l];
			for(int i=0; i<l; i++)
				v[i]=in.readLong();
			wall=BitSet.valueOf(v);
			}

		public void save(DataOutputStream out) throws IOException
			{
			out.writeUTF(name);
			out.writeInt(width);
			out.writeInt(height);
			long[] l=wall.toLongArray();
			out.writeInt(l.length);
			for(int i=0; i<l.length; i++)
				out.writeLong(l[i]);
			}

		public TileSet tileset() throws IOException
			{
			if(tileset==null)
				tileset=new TileSet(Gdx.files.internal("data/tileset/"+name), width, height);
			return tileset;
			}

		public boolean isWall(int i)
			{
			return wall.get(i);
			}

		public void toggleWall(int id)
			{
			wall.flip(id);
			}

		@Override
		public String toString()
			{
			return name;
			}
		}

	public static class MapEntry
		{
		/** position in tile */
		public int x, y;
		/** size in tile */
		public int w, h;
		private String tileset;
		public String name;
		private MapModel map;

		public MapEntry(int x, int y, int w, int h, String file, String tileset)
			{
			this.x=x;
			this.y=y;
			this.w=w;
			this.h=h;
			this.name=file;
			this.tileset=tileset;
			}

		public MapEntry(DataInputStream in) throws IOException
			{
			x=in.readInt();
			y=in.readInt();
			w=in.readInt();
			h=in.readInt();
			name=in.readUTF();
			tileset=in.readUTF();
			}

		public void write(DataOutputStream out) throws IOException
			{
			out.writeInt(x);
			out.writeInt(y);
			out.writeInt(w);
			out.writeInt(h);
			out.writeUTF(name);
			out.writeUTF(tileset);
			}

		public MapModel map() throws IOException
			{
			if(map==null)
				{
				try
					{
					FileHandle fh=Gdx.files.internal("data/maps/"+name+".map");
					DataInputStream in=new DataInputStream(fh.read());
					map=new MapModel(in);
					in.close();
					}
				catch (FileNotFoundException e)
					{
					map=new MapModel(w, h);
					}
				}
			return map;
			}

		public void save() throws IOException
			{
			if(map!=null)
				{
				DataOutputStream out=new DataOutputStream(new FileOutputStream("data/maps/"+name+".map"));
				map.save(out);
				out.close();
				}
			}

		public void setTileset(String name)
			{
			tileset=name;
			}

		public String tileset()
			{
			return tileset;
			}
		}
	}
