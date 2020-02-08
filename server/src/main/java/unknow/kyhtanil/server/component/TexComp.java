package unknow.kyhtanil.server.component;

import com.artemis.*;

public class TexComp extends PooledComponent {
	public String tex;

	@Override
	protected void reset() {
		tex = null;
	}
}
