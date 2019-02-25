package unknow.game.admin.screen;

import java.io.*;

import org.slf4j.*;
import org.slf4j.Logger;

import unknow.game.admin.graphics.*;
import unknow.kyhtanil.client.*;
import unknow.kyhtanil.common.maps.*;
import unknow.kyhtanil.common.maps.MapLayout.TilesetInfo;
import unknow.kyhtanil.common.util.*;
import unknow.scene.builder.*;

import com.badlogic.gdx.files.*;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.viewport.*;
import com.kotcrab.vis.ui.widget.*;
import com.kotcrab.vis.ui.widget.file.*;

public class TilesetEditor extends Actor
	{
	private static final Logger log=LoggerFactory.getLogger(TilesetEditor.class);

	protected ScreenViewport mapVp;
	protected ShapeRenderer sr=new ShapeRenderer();

	private VisTable tileset;
	private VisSelectBox<TilesetInfo> tilesetFile;
	private FileChooser fileChooser;

	private MapLayout layout;

	public TilesetEditor(MapLayout layout, SceneBuilder sceneBuilder)
		{
		this.layout=layout;
//		mapVp=new ScreenViewport();
//		mapVp.setUnitsPerPixel(Main.pixelToUnit(10));

//		sceneBuilder.addActor("tileset.open", new ChangeListener()
//			{
//			public void changed(ChangeEvent event, Actor actor)
//				{
//				fileChooser.setVisible(true);
//				fileChooser.setZIndex(TilesetEditor.this.getZIndex()+1);
//				}
//			});
//		sceneBuilder.addActor("tileset.show", new ChangeListener()
//			{
//			public void changed(ChangeEvent event, Actor actor)
//				{
//				showTileSet();
//				}
//			});
//
//		sceneBuilder.addActor("tileset.close", new ChangeListener()
//			{
//			public void changed(ChangeEvent event, Actor actor)
//				{
//				setVisible(false);
//				}
//			});
//		sceneBuilder.addActor("tileset.save", new ChangeListener()
//			{
//			public void changed(ChangeEvent event, Actor actor)
//				{
//				TilesetEditor.this.layout.setTilesetInfo(tilesetFile.getItems());
//				}
//			});
//
//		sceneBuilder.addListener(new SceneBuilder.Listener()
//			{
//
//			@Override
//			public void end(SceneBuilder builder, Wrapper<?> root)
//				{
//				tileset=builder.getActor("tileset.root");
//
//				tilesetFile=builder.getActor("tileset.file");
//				tilesetFile.setItems(ArrayUtils.newArray(TilesetEditor.this.layout.tilesetInfo()));
//
//				fileChooser=builder.getActor("tileset.fileChooser");
//				fileChooser.setListener(new FileChooserListener()
//					{
//					@Override
//					public void selected(Array<FileHandle> files)
//						{
//						Array<TilesetInfo> items=new Array<TilesetInfo>(tilesetFile.getItems());
//						items.ensureCapacity(files.size);
//						for(FileHandle tex:files)
//							{
//							try
//								{
//								items.add(new TilesetInfo(tex.name(), new TileSet(tex, 32, 32), 32, 32));
//								}
//							catch (Exception e)
//								{
//								log.warn("failed to load texture '"+tex.path()+"'", e);
//								}
//							}
//						tilesetFile.setItems(items);
//						}
//
//					@Override
//					public void selected(FileHandle file)
//						{
//
//						}
//
//					@Override
//					public void canceled()
//						{
//						fileChooser.setVisible(false);
//						}
//					});
//				}
//			});
		}

//	public void setup(Scene) throws SAXException, IOException, ParserConfigurationException
//		{
//		=new SceneBuilder();
//		
//		sceneBuilder.build("tileset.xml", this);
//
//		tileset=sceneBuilder.getActor("tileset");
//		tilesetFile=sceneBuilder.getActor("tilesetFile");
//		tilesetFile.setItems(ArrayUtils.newArray(layout.tilesetInfo()));
//		fileChooser=sceneBuilder.getActor("fileChooser");
//		fileChooser.setListener(new FileChooserListener()
//			{
//				@Override
//				public void selected(Array<FileHandle> files)
//					{
//					Array<TilesetInfo> items=new Array<TilesetInfo>(tilesetFile.getItems());
//					items.ensureCapacity(files.size);
//					for(FileHandle tex:files)
//						{
//						try
//							{
//							items.add(new TilesetInfo(tex.name(), new TileSet(tex, 32, 32), 32, 32));
//							}
//						catch (Exception e)
//							{
//							log.warn("failed to load texture '"+tex.path()+"'", e);
//							}
//						}
//					tilesetFile.setItems(items);
//					}
//
//				@Override
//				public void selected(FileHandle file)
//					{
//
//					}
//
//				@Override
//				public void canceled()
//					{
//					fileChooser.setVisible(false);
//					}
//			});
//		setVisible(false);
//		}

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

	public void showTileSet()
		{
		try
			{
			log.info("show");
			TilesetInfo tsi=tilesetFile.getSelected();
			TileSet s=tsi.tileset();
			tileset.clear();
			int w=s.width();
			for(int i=0; i<s.tileCount(); i++)
				{
				if(i%w==0)
					tileset.row();
				TileEditorActor a=new TileEditorActor(tsi, i);
				a.setSize(33, 33);
				tileset.add(a);
				}
			}
		catch (IOException e)
			{
			}
		}

	public static final class TileSetChooser extends FileChooser
		{
		public TileSetChooser()
			{
			super("Select tileset", FileChooser.Mode.OPEN);
			}

		@Override
		public void fadeOut()
			{
			setVisible(false);
			}
		}

	public void newTileset()
		{
		setVisible(true);
		fileChooser.setVisible(true);
		}
	}
