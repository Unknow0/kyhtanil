package unknow.game.admin.graphics;

import java.io.*;

import unknow.kyhtanil.common.maps.MapLayout.TilesetInfo;
import unknow.kyhtanil.common.maps.*;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type;

public class TileEditorActor extends Actor
	{
	private static final Texture c=new Texture(Gdx.files.internal("cross.png"));
	private int id;
	private TileSet ts;
	private TilesetInfo tsi;

	public TileEditorActor(TilesetInfo tsi, int id) throws IOException
		{
		this.tsi=tsi;
		this.ts=tsi.tileset();
		this.id=id;
		}

	public void draw(Batch batch, float parentAlpha)
		{
		batch.draw(ts.get(id), getX(), getY(), getWidth()-1, getHeight()-1);
		if(tsi.isWall(id))
			batch.draw(c, getX(), getY());
		}

	public boolean notify(Event event, boolean capture)
		{
		if(event instanceof InputEvent)
			{
			InputEvent e=(InputEvent)event;
			if(e.getType()==Type.touchDown)
				{
				tsi.toggleWall(id);
				e.stop();
				return true;
				}
			}
		return super.notify(event, capture);
		}
	}
