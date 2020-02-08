package unknow.kyhtanil.common.component;

import com.artemis.*;
import com.esotericsoftware.kryo.util.*;

public class Spirit extends PooledComponent {
	public transient int id;

	public IntMap<Integer> disciplines;
	public IntMap<Integer> skills;

	public long xp;
	public int level;
	public int points;

	protected void reset() {
		disciplines.clear();
		skills.clear();
	}
}
