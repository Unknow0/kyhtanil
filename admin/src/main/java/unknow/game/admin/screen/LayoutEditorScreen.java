package unknow.game.admin.screen;

import java.io.*;
import java.sql.*;

import unknow.game.admin.*;
import unknow.kyhtanil.client.*;
import unknow.kyhtanil.common.maps.*;

import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.viewport.*;
import com.kotcrab.vis.ui.widget.*;

public class LayoutEditorScreen extends AdminScreen implements InputProcessor
	{
	public static MapLayout.MapEntry selected=null;
	public static final Texture s=new Texture("selected.png");

	protected ScreenViewport mapVp;
	protected ShapeRenderer sr=new ShapeRenderer();

	private VisWindow edit;
	private VisTextField editName;
	private VisTextField editX;
	private VisTextField editY;
	private VisTextField editW;
	private VisTextField editH;

	private VisTextField x;
	private VisTextField y;
	private VisTextField w;
	private VisTextField h;

	private VisSelectBox<String> tileSetBox;

	public LayoutEditorScreen(MapLayout mapLayout) throws Exception
		{
		super(mapLayout);
		mapVp=new ScreenViewport();
		mapVp.setUnitsPerPixel(Main.pixelToUnit(10));

		sceneBuilder.addActor("new", new ChangeListener()
			{
				public void changed(ChangeEvent event, Actor actor)
					{
					try
						{
						tileSetBox.setItems(layout.tilesets());
						edit.pack();
						edit.center();
						edit.setVisible(true);
						}
					catch (Exception e)
						{
						e.printStackTrace();
						}
					}
			});
		sceneBuilder.addActor("save", new ChangeListener()
			{
				public void changed(ChangeEvent event, Actor actor)
					{
					try
						{
						save();
						}
					catch (Exception e)
						{
						e.printStackTrace();
						}
					}
			});
		sceneBuilder.addActor("show", new ChangeListener()
			{
				@Override
				public void changed(ChangeEvent event, Actor actor)
					{
					AdminMain.self.showEditor(selected);
					}
			});
		sceneBuilder.addActor("edit.ok", new ChangeListener()
			{
				public void changed(ChangeEvent event, Actor actor)
					{
					layout.add(Integer.parseInt(editX.getText()), Integer.parseInt(editY.getText()), Integer.parseInt(editW.getText()), Integer.parseInt(editH.getText()), editName.getText(), tileSetBox.getSelected());
					edit.setVisible(false);
					}
			});
		sceneBuilder.addActor("edit.cancel", new ChangeListener()
			{
				public void changed(ChangeEvent event, Actor actor)
					{
					edit.setVisible(false);
					}
			});

		sceneBuilder.build("layout.xml", stage.getRoot());

		edit=sceneBuilder.getActor("edit");
		edit.pack();
		editName=sceneBuilder.getActor("edit.name");
		editX=sceneBuilder.getActor("edit.x");
		editY=sceneBuilder.getActor("edit.y");
		editW=sceneBuilder.getActor("edit.w");
		editH=sceneBuilder.getActor("edit.h");

		x=sceneBuilder.getActor("x");
		y=sceneBuilder.getActor("y");
		w=sceneBuilder.getActor("w");
		h=sceneBuilder.getActor("h");

		tileSetBox=sceneBuilder.getActor("edit.tileset");
		}

	public void setPos(float x, float y)
		{
		float w=mapVp.getWorldWidth();
		float h=mapVp.getWorldHeight();
		if(x<w/2)
			x=w/2;

		if(y<h/2)
			y=h/2;

		mapVp.getCamera().position.set(x, y, 0);
		}

	public void move(float x, float y)
		{
		Vector3 p=mapVp.getCamera().position;
		setPos(p.x+x, p.y+y);
		}

	@Override
	public void resize(int width, int height)
		{
//		map.setSize(width-250, height-50);
		mapVp.update(width-200, height-34);

		Vector3 p=mapVp.getCamera().position;
		setPos(p.x, p.y);
//		stageVp.update(width, height);
//		stageVp.setScreenX(width-200);
		stage.getViewport().update(width, height, true);
		}

	@Override
	public void render(float delta)
		{
		Gdx.gl.glClearColor(.2f, .2f, .2f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		mapVp.apply();
		sr.setProjectionMatrix(mapVp.getCamera().combined);

		sr.begin(ShapeType.Filled);
		sr.setColor(Color.LIGHT_GRAY);
		for(MapLayout.MapEntry e:layout.maps())
			{
			if(selected==e)
				sr.setColor(Color.CHARTREUSE);
			sr.rect(e.x, e.y, e.w, e.h);
			if(selected==e)
				sr.setColor(Color.LIGHT_GRAY);
			}
		sr.end();

		sr.begin(ShapeType.Line);
		sr.setColor(Color.WHITE);
		for(MapLayout.MapEntry e:layout.maps())
			{
			if(selected==e)
				sr.setColor(Color.RED);
			sr.rect(e.x, e.y, e.w, e.h);
			if(selected==e)
				sr.setColor(Color.WHITE);
			}
		sr.end();

		stage.getViewport().apply();
		stage.act(delta);
		stage.draw();
		}

	@Override
	public void show()
		{
		Gdx.input.setInputProcessor(new InputMultiplexer(stage, this));
		}

	private void save() throws IOException, SQLException
		{
		FileOutputStream fos=new FileOutputStream("data/maps.layout");
		DataOutputStream dos=new DataOutputStream(fos);
		layout.save(dos);
		dos.close();
		}

	@Override
	public boolean keyDown(int keycode)
		{
		if(Keys.ESCAPE==keycode)
			selected=null;
		return false;
		}

	@Override
	public boolean keyUp(int keycode)
		{
		return false;
		}

	@Override
	public boolean keyTyped(char character)
		{
		return false;
		}

	private long lastClic=0;

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button)
		{
		Vector2 v=mapVp.unproject(new Vector2(screenX, screenY));
		selected=layout.get(v.x, v.y);
		if(selected!=null)
			{
			if(System.currentTimeMillis()-lastClic<200)
				{
				AdminMain.self.showEditor(selected);
				return true;
				}
			lastClic=System.currentTimeMillis();
			x.setText(String.valueOf(selected.x));
			y.setText(String.valueOf(selected.y));
			w.setText(String.valueOf(selected.w));
			h.setText(String.valueOf(selected.h));
			}
		else
			{
			}
		return false;
		}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
		{
		return false;
		}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer)
		{
		move(-Gdx.input.getDeltaX(pointer)*mapVp.getUnitsPerPixel(), Gdx.input.getDeltaY(pointer)*mapVp.getUnitsPerPixel());
		return true;
		}

	public boolean mouseMoved(int screenX, int screenY)
		{
		return false;
		}

	@Override
	public boolean scrolled(int amount)
		{
		mapVp.setUnitsPerPixel(mapVp.getUnitsPerPixel()+amount*.01f);
		mapVp.update(mapVp.getScreenWidth(), mapVp.getScreenHeight(), false);
		move(0, 0);
		return true;
		}

//	@Override
//	protected void setTileSet(Array<String> tileset)
//		{
//		tileSetBox.setItems(tileset);
//		edit.pack();
//		}
	}
