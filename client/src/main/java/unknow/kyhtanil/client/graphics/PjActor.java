package unknow.kyhtanil.client.graphics;

import unknow.kyhtanil.common.*;

import com.badlogic.gdx.graphics.*;

public class PjActor extends MobActor
	{
	private float nextUpdate=.25f;

	public PjActor(Texture tex, UUID uuid, int hp)
		{
		super(tex, uuid, hp);
		}

	public void act(float delta)
		{
		super.act(delta);

		nextUpdate-=delta;

//		if(nextUpdate<=0)
//			{
//			try
//				{
//				Main.co().update(uuid, x, y, dir);
//				nextUpdate=.25f;
//				}
//			catch (IOException e)
//				{
//				e.printStackTrace(); // TODO
//				}
//			}
		}

	public void setUuid(UUID uuid)
		{
		this.uuid=uuid;
		}

	}
