package unknow.kyhtanil.common.component.net;

import com.artemis.PooledComponent;

import unknow.kyhtanil.common.component.MobInfoComp;
import unknow.kyhtanil.common.component.PositionComp;
import unknow.kyhtanil.common.component.SpriteComp;
import unknow.kyhtanil.common.component.VelocityComp;
import unknow.kyhtanil.common.pojo.UUID;

public class Spawn extends PooledComponent
	{
	public UUID uuid=null;
	public SpriteComp sprite=null;
	public MobInfoComp m;
	public PositionComp p;
	public VelocityComp v;

	public Spawn()
		{
		}

	public Spawn(UUID uuid, SpriteComp sprite, MobInfoComp m, PositionComp p, VelocityComp v)
		{
		this.uuid=uuid;
		this.sprite=new SpriteComp(sprite);
		this.m=m;
		this.p=p;
		this.v=v;
		}

	public void reset()
		{
		uuid=null;
		sprite=null;
		m=null;
		p=null;
		v=null;
		}

	@Override
	public String toString()
		{
		return "Spawn "+uuid+" "+sprite.tex;
		}
	}