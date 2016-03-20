package unknow.kyhtanil.common.util;

import java.security.*;

import com.artemis.*;
import com.esotericsoftware.kryo.*;
import com.esotericsoftware.kryo.io.*;

import unknow.common.kryo.*;
import unknow.kyhtanil.common.*;
import unknow.kyhtanil.common.pojo.*;

public class Kryos extends KryoWrap
	{
	private ArtemisInstantiator instantiator;

	@SafeVarargs
	public Kryos(World world, Class<? extends Component>... comp) throws NoSuchAlgorithmException
		{
		addClass(ErrorComp.class);
		addClass(Login.class);
		addClass(LogResult.class);
		addClass(LogChar.class);
		addClass(PjInfo.class);
		addClass(Spawn.class);
		addClass(Despawn.class);
		addClass(Move.class);
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
