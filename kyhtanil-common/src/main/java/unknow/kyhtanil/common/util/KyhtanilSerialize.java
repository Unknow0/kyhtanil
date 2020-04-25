package unknow.kyhtanil.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;

import unknow.kyhtanil.common.component.ErrorComp;
import unknow.kyhtanil.common.component.StatAgg;
import unknow.kyhtanil.common.component.StatBase;
import unknow.kyhtanil.common.component.StatShared;
import unknow.kyhtanil.common.component.account.CreateAccount;
import unknow.kyhtanil.common.component.account.CreateChar;
import unknow.kyhtanil.common.component.account.LogChar;
import unknow.kyhtanil.common.component.account.LogResult;
import unknow.kyhtanil.common.component.account.Login;
import unknow.kyhtanil.common.component.account.PjInfo;
import unknow.kyhtanil.common.component.net.Attack;
import unknow.kyhtanil.common.component.net.DamageReport;
import unknow.kyhtanil.common.component.net.Despawn;
import unknow.kyhtanil.common.component.net.Move;
import unknow.kyhtanil.common.component.net.Spawn;
import unknow.kyhtanil.common.component.net.UpdateInfo;
import unknow.kyhtanil.common.pojo.Point;
import unknow.serialize.binary.BinaryFormat;
import unknow.serialize.binary.BinaryFormat.Builder;

public class KyhtanilSerialize {
	private static final BinaryFormat format;
	static {
		Builder create = BinaryFormat.create();
		create.register(ErrorComp.class);
		create.register(Login.class);
		create.register(CreateAccount.class);
		create.register(CreateChar.class);
		create.register(LogResult.class);
		create.register(LogChar.class);
		create.register(PjInfo.class);
		create.register(Spawn.class);
		create.register(Despawn.class);
		create.register(Move.class);
		create.register(UpdateInfo.class);
		create.register(Attack.class);
		create.register(Point.class);
		create.register(DamageReport.class);
		create.register(StatBase.class);
		create.register(StatShared.class);
		create.register(StatAgg.class);

		try {
			format = create.build();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}

	public static byte[] hash() {
		return format.hash();
	}

	public static Object read(InputStream in) throws IOException {
		return format.read(in);
	}

	public static void write(Object o, OutputStream out) throws IOException {
		format.write(o, out);
	}
}
