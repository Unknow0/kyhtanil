package unknow.kyhtanil.common.util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.pool.KryoFactory;
import com.esotericsoftware.kryo.pool.KryoPool;
import com.esotericsoftware.minlog.Log;

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

public class Kryos implements KryoFactory {
	private static final Logger log = LoggerFactory.getLogger(Kryos.class);
	private KryoPool pool;
	private List<Class<?>> clazz;
	private MessageDigest md;
	private boolean initDone = false;
	private byte[] hash;

	private static final int STATIC_TRANSIANT = Modifier.STATIC | Modifier.TRANSIENT;

	public Kryos() throws NoSuchAlgorithmException {
		clazz = new ArrayList<Class<?>>();
		md = MessageDigest.getInstance("SHA-512");
		pool = new KryoPool.Builder(this).build();

		addClass(ErrorComp.class);
		addClass(Login.class);
		addClass(CreateAccount.class);
		addClass(CreateChar.class);
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
		addClass(StatBase.class);
		addClass(StatShared.class);
		addClass(StatAgg.class);
		doneInit();
	}

	public Kryo create() {
		if (!initDone)
			throw new IllegalStateException("can't create kryo before init if done");
		Kryo kryo = new Kryo();
		kryo.setRegistrationRequired(true);
		for (Class<?> c : clazz)
			kryo.register(c);
		init(kryo);
		return kryo;
	}

	public void addClass(Class<?> c) {
		if (initDone)
			throw new IllegalStateException("can't add class after a kryo was created");
		if (clazz.contains(c))
			return;
		log.debug("Register '{}'", c.getSimpleName());
		clazz.add(c);
		if (c.isArray()) {
			addClass(c.getComponentType());
			return; // don't hash array
		}
		long h = 0;
		try {
			h = hash(c, 0);
		} catch (UnsupportedEncodingException e) {
			Log.warn("Unsuported encoding 'UTF8' protocol integrity won't be checked");
		}
		md.update((byte) (h & 0xFF));
		md.update((byte) ((h >> 8) & 0xFF));
		md.update((byte) ((h >> 16) & 0xFF));
		md.update((byte) ((h >> 24) & 0xFF));
		md.update((byte) ((h >> 32) & 0xFF));
		md.update((byte) ((h >> 40) & 0xFF));
		md.update((byte) ((h >> 48) & 0xFF));
		md.update((byte) ((h >> 56) & 0xFF));
	}

	public byte[] hash() {
		return hash;
	}

	public void doneInit() {
		initDone = true;
		hash = md.digest();
		md = null;
	}

	protected long hash(Class<?> c, long h) throws UnsupportedEncodingException {
		if (c == Object.class || c == null || c == Enum.class || c.isPrimitive() || c==String.class || clazz.contains(c))
			return h;
		Field[] fields = c.getDeclaredFields();
		Arrays.sort(fields, (a, b) -> a.getName().compareTo(b.getName()));
		for (Field f : fields) {
			if ((f.getModifiers() & STATIC_TRANSIANT) != 0)
				continue;
			addClass(f.getType());
			h = 17 * h + hash(f.toString().getBytes("UTF8"));
		}
		return hash(c.getSuperclass(), h);
	}

	public static long hash(byte[] b) {
		if (b.length == 0)
			return 0;
		long h = 0;
		for (int i = 0; i < b.length; i++)
			h = 31 * h + b[i];
		return h;
	}

	public void write(Output output, Object object) {
		Kryo kryo = pool.borrow();
		try {
			kryo.writeClassAndObject(output, object);
			output.flush();
		} finally {
			pool.release(kryo);
		}
	}

	public Object read(Input input) {
		Kryo kryo = pool.borrow();
		try {
			return kryo.readClassAndObject(input);
		} finally {
			pool.release(kryo);
		}
	}

	protected void init(Kryo kryo) {
		kryo.setReferences(false);
	}
}
