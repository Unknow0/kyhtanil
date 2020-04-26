package unknow.kyhtanil.common.util;

import java.util.*;

import com.badlogic.gdx.utils.*;

public class ArrayUtils {
	public static <T> Array<T> newArray(Collection<T> col) {
		Array<T> a = new Array<>(false, col.size());
		for (T t : col)
			a.add(t);
		return a;
	}

	public static <T> void addAll(Array<T> a, Collection<T> col) {
		a.ensureCapacity(col.size());
		for (T t : col)
			a.add(t);
	}
}
