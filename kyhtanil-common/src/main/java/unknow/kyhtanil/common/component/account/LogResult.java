package unknow.kyhtanil.common.component.account;

import com.artemis.PooledComponent;
import javax.annotation.Generated;
import unknow.kyhtanil.common.pojo.CharDesc;
import unknow.kyhtanil.common.pojo.UUID;

@Generated(value="class unknow.pojo.Generator", date="2017-05-01T19:13:39+0200")
public class LogResult extends PooledComponent {
	public UUID uuid=null;
	public CharDesc[] characters=null;

	public LogResult() {
	}

	public LogResult(UUID uuid, CharDesc[] characters) {
		this.uuid=uuid;
		this.characters=characters;
	}

	public void reset() {
		uuid=null;
		characters=null;
	}

	public String toString() {
		return "uuid: "+uuid+", characters: "+characters;
	}
}