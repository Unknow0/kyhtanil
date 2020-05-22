package unknow.kyhtanil.client.graphics;

import java.util.Iterator;

import unknow.kyhtanil.client.system.State;
import unknow.kyhtanil.common.pojo.CharDesc;

/**
 * the char list
 * 
 * @author unknow
 */
public class CharList extends CustomList<CharActor> {
	private CharDesc[] cache;

	@Override
	protected Iterator<CharActor> content() {
		return new It();
	}

	@Override
	public void validate() {
		if (State.state.chars != cache) {
			cache = State.state.chars;
			invalidate();
		}
		super.validate();
	}

	/**
	 * @return the selected char
	 */
	public CharDesc selected() {
		return selection.c;
	}

	private class It implements Iterator<CharActor> {
		private int i = 0;

		@Override
		public boolean hasNext() {
			return i < cache.length;
		}

		@Override
		public CharActor next() {
			return new CharActor(cache[i++]);
		}
	}
}
