package unknow.kyhtanil.client.system.net;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

import unknow.kyhtanil.client.Main;
import unknow.kyhtanil.common.component.ErrorComp;

/**
 * manage received error
 * 
 * @author unknow
 */
public class ErrorSystem extends IteratingSystem {
	private ComponentMapper<ErrorComp> error;
	private Main main;

	/**
	 * create new ErrorSystem
	 * 
	 * @param main
	 */
	public ErrorSystem(Main main) {
		super(Aspect.all(ErrorComp.class));
		this.main = main;
	}

	@Override
	protected void process(int entityId) {
		ErrorComp e = error.get(entityId);
		world.delete(entityId);
		main.error(e.code);
	}
}
