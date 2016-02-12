package unknow.kyhtanil.client.graphics;

import unknow.kyhtanil.client.*;

import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.viewport.*;

public class TileMap
	{
	protected MapModel map;
	protected TileSet tileset;
	protected float gap=0;

	public TileMap(MapModel map, TileSet tileset)
		{
		this.map=map;
		this.tileset=tileset;
		}

	public TileMap(MapModel map, TileSet tileset, float gap)
		{
		this(map, tileset);
		this.gap=gap;
		}

	public void draw(Batch batch, Viewport vp)
		{
		if(tileset==null||map==null)
			return;
		float w=tileset.tileWidth();
		float h=tileset.tileHeight();
		float wg=w+gap;
		float hg=w+gap;

		Vector3 v=vp.getCamera().position;
		int sx=(int)((v.x-vp.getWorldWidth()/2)/wg);
		int sy=(int)((v.y-vp.getWorldHeight()/2)/hg);
		if(sx<0)
			sx=0;
		if(sy<0)
			sy=0;
		int ex=sx+1+(int)(vp.getWorldWidth()/wg);
		int ey=sy+1+(int)(vp.getWorldHeight()/hg);
		if(ex>map.width())
			ex=map.width();
		if(ey>map.height())
			ey=map.height();

		for(int i=sx; i<ex; i++)
			{
			for(int j=sy; j<ey; j++)
				{
				TextureRegion tex=tileset.get(map.get(i, j));
				batch.draw(tex, i*wg, j*hg, w, h);
				}
			}
		}

	public MapModel getMap()
		{
		return map;
		}

	public void setMap(MapModel map)
		{
		this.map=map;
		}

	public TileSet getTileset()
		{
		return tileset;
		}

	public void setTileset(TileSet tileset)
		{
		this.tileset=tileset;
		}

	public void setTile(float x, float y, byte i)
		{
		map.set((int)(x/(tileset.tileWidth()+gap)), (int)(y/(tileset.tileHeight()+gap)), i);
		}
	}
