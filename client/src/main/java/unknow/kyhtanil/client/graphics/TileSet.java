package unknow.kyhtanil.client.graphics;

import unknow.kyhtanil.client.*;

import com.badlogic.gdx.files.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;

public class TileSet
	{
	/** full texture */
	private Texture texture;
	/** tile size*/
	private int width, height;

	private TextureRegion[] tiles;

	public TileSet(FileHandle file, int w, int h)
		{
		texture=new Texture(file);
		width=w;
		height=h;

		int texWidth=texture.getWidth()/width;
		int texHeight=texture.getHeight()/height;
		tiles=new TextureRegion[texHeight*texWidth];
		for(int x=0; x<texWidth; x++)
			{
			for(int y=0; y<texHeight; y++)
				{
				tiles[x+y*texHeight]=new TextureRegion(texture, x*width, y*height, width, height);
				}
			}
		}

	public TextureRegion get(int i)
		{
		return tiles[i];
		}

	public int tileCount()
		{
		return tiles.length;
		}

	public float tileWidth()
		{
		return Main.pixelToUnit(width);
		}

	public float tileHeight()
		{
		return Main.pixelToUnit(height);
		}

	public void dispose()
		{
		tiles=null;
		texture.dispose();
		texture=null;
		}
	}
