package unknow.game.admin.screen;

import java.io.*;
import java.util.*;

import unknow.game.admin.*;
import unknow.game.admin.graphics.*;
import unknow.kyhtanil.client.*;
import unknow.kyhtanil.common.maps.*;
import unknow.kyhtanil.common.maps.MapLayout.TilesetInfo;

import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.viewport.*;
import com.kotcrab.vis.ui.widget.*;

public class MapEditorScreen extends AdminScreen implements EventListener
	{
	public static TileActor selected;
	public static final Texture s=new Texture("selected.png");

	protected MapLayout.MapEntry entry;
	protected MapModel map;
	protected TileSet t;
	protected ScreenViewport mapVp;
	protected Batch batch;

	private VisTable tileSet;
	private Cell<?> mapCell;
	private VisTextField name;

	private VisSelectBox<String> tileSetBox;

	public MapEditorScreen(MapLayout layout) throws Exception
		{
		super(layout);
		batch=new SpriteBatch();
		mapVp=new ScreenViewport();
		mapVp.setUnitsPerPixel(Main.pixelToUnit(1));

		sceneBuilder.addActor("save", new ChangeListener()
			{
				public void changed(ChangeEvent event, Actor actor)
					{
					try
						{
						entry.save();
						}
					catch (Exception e)
						{
						e.printStackTrace();
						}
					}
			});
		sceneBuilder.addActor("tilesetChange", new ChangeListener()
			{
				public void changed(ChangeEvent e, Actor actor)
					{
					try
						{
						showTileset();
						}
					catch (Exception ex)
						{
						ex.printStackTrace();
						}
					}
			});

		sceneBuilder.build("map.xml", stage.getRoot());

		name=sceneBuilder.getActor("name");
		tileSetBox=sceneBuilder.getActor("tilesetBox");
		tileSet=sceneBuilder.getActor("tileset");
		mapCell=sceneBuilder.getActor("map");
		stage.addCaptureListener(this);
		}

	private void showTileset() throws IOException
		{
		if(entry==null)
			return;
		entry.setTileset(tileSetBox.getSelected());
		t=layout.tileset(entry.tileset());

		tileSet.clear();
		for(int i=0; i<t.tileCount(); i++)
			{
			TileActor a=new TileActor(t.get(i), (byte)i);
			a.setSize(33, 33);
			tileSet.add(a);

			if(i+1<t.tileCount()&&(i+1)%6==0)
				tileSet.row();
			}
		}

	public void show()
		{
		Gdx.input.setInputProcessor(new InputMultiplexer(stage));

//		try
//			{
//			File df=new File("data");
//			if(!df.exists())
//				{
//				loader.show();
//				SyncClient sync=new SyncClient(Cfg.getSystemString("updater.host"), Cfg.getSystemInt("updater.port"), "./");
//				sync.setListener(loader);
//				sync.update(Cfg.getSystemString("updater.login"), Cfg.getSystemString("updater.pass"), Cfg.getSystemString("updater.project"), false, Pattern.compile("data/*"));
//				sync.close();
//				}
//			tileSetBox.setItems(layout.tilesets());
//			}
//		catch (Exception e)
//			{
//			e.printStackTrace();
//			}
		}

	public void setEntry(MapLayout.MapEntry entry) throws IOException
		{
		this.entry=entry;
		Collection<TilesetInfo> tilesetInfo=layout.tilesetInfo();
		Array<String> a=new Array<String>(tilesetInfo.size());
		for(TilesetInfo t:tilesetInfo)
			a.add(t.name);
		tileSetBox.setItems(a);
		tileSetBox.setSelected(entry.tileset());
		name.setText(entry.name);
		map=entry.map();
		setPos(entry.x, entry.y);
		}

	public void setPos(float x, float y)
		{
		float tw=layout.tileWidth()*mapVp.getUnitsPerPixel();
		float th=layout.tileHeight()*mapVp.getUnitsPerPixel();

		float ex=entry.x*tw;
		float ey=entry.y*th;
		float w=mapVp.getWorldWidth();
		float h=mapVp.getWorldHeight();
		if(x<ex+w/2)
			x=ex+w/2;
		if(x>ex+map.width()*tw-w/2)
			x=ex+map.width()*w-w/2;

		if(y<ey+h/2)
			y=ey+h/2;
		if(h>ey+map.height()*th-h/2)
			h=ey+map.height()*th-h/2;

		mapVp.getCamera().position.set(x, y, 0);
		}

	public void move(float x, float y)
		{
		Vector3 p=mapVp.getCamera().position;
		setPos(p.x+x, p.y+y);
		}

	public void resize(int width, int height)
		{
		stage.getViewport().update(width, height, true);
		Table root=mapCell.getTable();
		root.validate();
		root.layout();

		mapVp.update((int)mapCell.getActorWidth(), (int)mapCell.getActorHeight());
		move(0, 0);
		}

	public void render(float delta)
		{
		Gdx.gl.glClearColor(.2f, .2f, .2f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		mapVp.apply();
		batch.setProjectionMatrix(mapVp.getCamera().combined);
		batch.begin();
		try
			{
			layout.draw(batch, .125f, mapVp);
			}
		catch (IOException e)
			{
			e.printStackTrace();
			}
		batch.end();

		stage.getViewport().apply();
		stage.act(delta);
		stage.draw();
		}

	@Override
	public boolean handle(Event e)
		{
		if(e instanceof InputEvent)
			{
			InputEvent ie=(InputEvent)e;
			switch (ie.getType())
				{
				case keyDown:
					if(selected!=null&&Keys.ESCAPE==ie.getKeyCode())
						{
						selected=null;
						return true;
						}
					break;
				case scrolled:
					mapVp.setUnitsPerPixel(mapVp.getUnitsPerPixel()-ie.getScrollAmount()*.01f);
					mapVp.update(mapVp.getScreenWidth(), mapVp.getScreenHeight(), false);
					Vector2 p=mapVp.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
					setPos(p.x, p.y);
					return true;
				case touchDragged:
					if(selected==null)
						{
						move(-Gdx.input.getDeltaX()*mapVp.getUnitsPerPixel(), Gdx.input.getDeltaY()*mapVp.getUnitsPerPixel());
						return true;
						}
				case touchDown:
					if(selected!=null)
						{
						MapLayout layout=AdminMain.self.layout;
						float tw=layout.tileWidth()*.125f;
						float th=layout.tileHeight()*.125f;
						Vector2 v=mapVp.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
						float x=v.x/tw-entry.x;
						float y=v.y/th-entry.y;
						map.set((int)x, (int)y, selected.id);
						return true;
						}
					return true;
				default:
				}
			}
		return false;
		}
	}
