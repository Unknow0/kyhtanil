/**
 * 
 */
package unknow.kyhtanil.client;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.IntMap;

/**
 * @author unknow
 */
@SuppressWarnings("javadoc")
public enum Keybind {
	// move
	UP, DOWN, LEFT, RIGHT, SHOW_STAT,

	BAR_1, BAR_2, BAR_3, BAR_4, BAR_5, BAR_6, BAR_7, BAR_8, BAR_9;

	private static final IntMap<Keybind> bind = new IntMap<>();

	public static void load() { // TODO
		bind.put(Input.Keys.Z, UP);
		bind.put(Input.Keys.S, DOWN);
		bind.put(Input.Keys.Q, LEFT);
		bind.put(Input.Keys.D, RIGHT);

		bind.put(Input.Keys.C, SHOW_STAT);

		bind.put(Input.Keys.NUM_1, BAR_1);
		bind.put(Input.Keys.NUM_2, BAR_2);
		bind.put(Input.Keys.NUM_3, BAR_3);
		bind.put(Input.Keys.NUM_4, BAR_4);
		bind.put(Input.Keys.NUM_5, BAR_5);
		bind.put(Input.Keys.NUM_6, BAR_6);
		bind.put(Input.Keys.NUM_7, BAR_7);
		bind.put(Input.Keys.NUM_8, BAR_8);
		bind.put(Input.Keys.NUM_9, BAR_9);
	}

	public static Keybind get(int key) {
		return bind.get(key);
	}
}
