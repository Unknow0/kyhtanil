package unknow.kyhtanil.common.component.net;

import com.artemis.Component;

import unknow.kyhtanil.common.component.StatShared;
import unknow.kyhtanil.common.component.PositionComp;
import unknow.kyhtanil.common.component.SpriteComp;
import unknow.kyhtanil.common.component.VelocityComp;
import unknow.kyhtanil.common.pojo.UUID;

public class Spawn extends Component {
	public UUID uuid = null;
	public SpriteComp sprite = null;
	public StatShared m;
	public PositionComp p;
	public VelocityComp v;

	public Spawn() {
	}

	public Spawn(UUID uuid, SpriteComp sprite, StatShared m, PositionComp p, VelocityComp v) {
		this.uuid = uuid;
		this.sprite = new SpriteComp(sprite);
		this.m = m;
		this.p = p;
		this.v = v;
	}

	@Override
	public String toString() {
		return "Spawn " + uuid + " " + sprite.tex;
	}
}