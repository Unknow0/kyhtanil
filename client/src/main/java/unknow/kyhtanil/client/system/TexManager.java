package unknow.kyhtanil.client.system;

import unknow.common.data.*;
import unknow.kyhtanil.client.component.*;

import com.artemis.*;
import com.artemis.utils.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;

public class TexManager extends BaseEntitySystem
	{
	private ComponentMapper<SpriteComp> sprite;

	private BTree<String,Ref> cache=new BTree<String,Ref>();

	public TexManager()
		{
		super(Aspect.all(SpriteComp.class));
		}

	@Override
	public void inserted(IntBag entities)
		{
		String[] r=new String[entities.size()];
		for(int i=0; i<entities.size(); i++)
			{
			String t=sprite.get(entities.get(i)).tex;
			r[i]=t;
			if(!cache.containsKey(t))
				cache.put(t, new Ref(t));
			}
		Gdx.app.postRunnable(new Loader(r));
		}

	@Override
	public void removed(IntBag entities)
		{
		for(int i=0; i<entities.size(); i++)
			{
			SpriteComp t=sprite.get(entities.get(i));
			if(t==null)
				continue;
			Ref ref=cache.get(t.tex);
			if(ref!=null&&ref.unref())
				cache.remove(t.tex);
			}
		}

	@Override
	protected void processSystem()
		{
		}

	public TextureRegion get(String tex)
		{
		Ref ref=cache.get(tex);
		return ref==null?null:ref.tex;
		}

	private static class Ref
		{
		int count=0;
		String path;
		TextureRegion tex;

		public Ref(String path)
			{
			this.path=path;
			}

		public void ref()
			{
			if(count==0)
				{
				if(tex==null)
					tex=new TextureRegion(new Texture(Gdx.files.internal(path)));
				}
			count++;
			}

		public boolean unref()
			{
			if(count--<=0&&tex!=null)
				tex.getTexture().dispose();
			return count==0;
			}
		}

	private class Loader implements Runnable
		{
		private String[] t;

		public Loader(String[] t)
			{
			this.t=t;
			}

		public void run()
			{
			for(int i=0; i<t.length; i++)
				{
				Ref ref=cache.get(t[i]);
				if(ref!=null)
					ref.ref();
				}
			}
		}

	}
