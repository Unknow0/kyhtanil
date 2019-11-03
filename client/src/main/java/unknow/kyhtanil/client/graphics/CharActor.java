package unknow.kyhtanil.client.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;

import unknow.kyhtanil.common.pojo.CharDesc;

public class CharActor extends VisLabel
	{
	protected CharDesc c;
	private boolean selected;
	private final Drawable border=VisUI.getSkin().getDrawable("border");

	public CharActor(CharDesc c)
		{
		super(c.name+" ("+c.level+")");
		this.c=c;
		}

	@Override
	public float getPrefHeight()
		{
		return super.getPrefHeight()+4;
		}

	@Override
	public float getPrefWidth()
		{
		return super.getPrefWidth()+10;
		}

	@Override
	public void draw(Batch batch, float parentAlpha)
		{
		super.draw(batch, parentAlpha);
		if(selected)
			border.draw(batch, getX(), getY(), getWidth(), getHeight());
		}

	public void setSelected(boolean b)
		{
		selected=b;
		}
	}