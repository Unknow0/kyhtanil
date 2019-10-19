package unknow.kyhtanil.common.util;

import java.security.*;

import unknow.common.kryo.*;
import unknow.kyhtanil.common.component.*;
import unknow.kyhtanil.common.component.account.*;
import unknow.kyhtanil.common.component.net.*;
import unknow.kyhtanil.common.pojo.*;

import com.artemis.*;
import com.esotericsoftware.kryo.*;
import com.esotericsoftware.kryo.io.*;

public class Kryos extends KryoWrap
	{
	private ArtemisInstantiator instantiator;

	@SafeVarargs
	public Kryos(World world, Class<? extends Component>... comp) throws NoSuchAlgorithmException
		{
		addClass(ErrorComp.class);
		addClass(Login.class);
		addClass(CreateAccount.class);
		addClass(LogResult.class);
		addClass(LogChar.class);
		addClass(PjInfo.class);
		addClass(Spawn.class);
		addClass(Despawn.class);
		addClass(Move.class);
		addClass(UpdateInfo.class);
		addClass(Attack.class);
		addClass(Point.class);
		addClass(DamageReport.class);
		doneInit();

		instantiator=new ArtemisInstantiator(world, comp);
		}

	protected void init(Kryo kryo)
		{
		super.init(kryo);
		kryo.setInstantiatorStrategy(instantiator);
		}

	public Object read(Input input)
		{
		// reset uuidEntity
		ArtemisInstantiator.reset();
		return super.read(input);
		}
	}
