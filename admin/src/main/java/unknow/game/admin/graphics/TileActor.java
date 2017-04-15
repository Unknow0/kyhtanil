package unknow.game.admin.graphics;

import unknow.game.admin.screen.*;

import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type;

public class TileActor extends Actor
	{

	private TextureRegion tex;
	public byte id;

	public TileActor(TextureRegion tex, byte id)
		{
		this.tex=tex;
		this.id=id;
		}

	public void draw(Batch batch, float parentAlpha)
		{
		if(MapEditorScreen.selected==this)
			batch.draw(MapEditorScreen.s, getX(), getY(), getWidth(), getHeight());
		batch.draw(tex, getX()+2, getY()+2, getWidth()-4, getHeight()-4);
		}

	public boolean notify(Event event, boolean capture)
		{
		if(event instanceof InputEvent)
			{
			InputEvent e=(InputEvent)event;
			if(e.getType()==Type.touchDown)
				{
				MapEditorScreen.selected=this;
				event.stop();
				return true;
				}
			}
		return super.notify(event, capture);
		}
	}
