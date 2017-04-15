package unknow.kyhtanil.common.maps;

import java.io.*;

import com.badlogic.gdx.files.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;

public class TileSet
	{
	/** full texture */
	private Texture texture;

	private TextureRegion[] tiles;

	public TileSet(FileHandle tex, int width, int height)
		{
		texture=new Texture(tex);

		int texWidth=texture.getWidth()/width;
		int texHeight=texture.getHeight()/height;
		tiles=new TextureRegion[texHeight*texWidth];
		for(int x=0; x<texWidth; x++)
			{
			for(int y=0; y<texHeight; y++)
				tiles[x+y*texWidth]=new TextureRegion(texture, x*width, y*height, width, height);
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

	public void dispose()
		{
		tiles=null;
		texture.dispose();
		texture=null;
		}
	}
