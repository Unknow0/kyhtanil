package unknow.kyhtanil.common.component.account;

import java.util.Arrays;

import com.artemis.Component;

import unknow.kyhtanil.common.pojo.CharDesc;
import unknow.kyhtanil.common.pojo.UUID;

public class LogResult extends Component {
	public UUID uuid = null;
	public CharDesc[] characters = null;

	public LogResult() {
	}

	public LogResult(UUID uuid, CharDesc[] characters) {
		this.uuid = uuid;
		this.characters = characters;
	}

	@Override
	public String toString() {
		return "LogResult [uuid=" + uuid + ", characters=" + Arrays.toString(characters) + "]";
	}
}