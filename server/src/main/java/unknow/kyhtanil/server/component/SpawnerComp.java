package unknow.kyhtanil.server.component;

import com.artemis.*;

public class SpawnerComp extends PooledComponent {
	public float x, y;
	public float range;

	public int max_count;
	public int current_count;

	public float creation_speed;
	public float current;

	protected void reset() {
		x = y = range = 0;
		max_count = current_count = 0;
		creation_speed = current = 0;
	}
}
