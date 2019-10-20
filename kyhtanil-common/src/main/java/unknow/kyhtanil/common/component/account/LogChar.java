package unknow.kyhtanil.common.component.account;

import javax.annotation.Generated;

import com.artemis.Component;

import unknow.kyhtanil.common.pojo.UUID;

@Generated(value="class unknow.pojo.Generator", date="2017-05-01T19:13:39+0200")
public class LogChar extends Component {
	public UUID uuid=null;
	public int character=0;

	public LogChar() {
	}

	public LogChar(UUID uuid, int character) {
		this.uuid=uuid;
		this.character=character;
	}

	public String toString() {
		return "uuid: "+uuid+", character: "+character;
	}
}