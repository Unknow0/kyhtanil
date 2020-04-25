package unknow.kyhtanil.common.component.account;

import com.artemis.Component;

import unknow.kyhtanil.common.pojo.UUID;

public class LogChar extends Component {
	public UUID uuid = null;
	public int character = 0;

	public LogChar() {
	}

	public LogChar(UUID uuid, int character) {
		this.uuid = uuid;
		this.character = character;
	}

	@Override
	public String toString() {
		return "LogChar [uuid=" + uuid + ", character=" + character + "]";
	}

}