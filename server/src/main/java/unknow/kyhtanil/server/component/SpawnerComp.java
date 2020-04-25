package unknow.kyhtanil.server.component;

import com.artemis.*;

public class SpawnerComp extends PooledComponent {
	public float x, y;
	public float r;

	public int max;
	public int current_count;

	public float speed;
	public float current;

	@Override
	protected void reset() {
		x = y = r = 0;
		max = current_count = 0;
		speed = current = 0;
	}
}
