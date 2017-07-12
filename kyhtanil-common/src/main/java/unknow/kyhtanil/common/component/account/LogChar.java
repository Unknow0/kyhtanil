package unknow.kyhtanil.common.component.account;

import com.artemis.PooledComponent;
import javax.annotation.Generated;
import unknow.kyhtanil.common.pojo.UUID;

@Generated(value="class unknow.pojo.Generator", date="2017-05-01T19:13:39+0200")
public class LogChar extends PooledComponent {
	public UUID uuid=null;
	public int character=0;

	public LogChar() {
	}

	public LogChar(UUID uuid, int character) {
		this.uuid=uuid;
		this.character=character;
	}

	public void reset() {
		uuid=null;
		character=0;
	}

	public String toString() {
		return "uuid: "+uuid+", character: "+character;
	}
}