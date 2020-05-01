package unknow.kyhtanil.common.component.account;

import java.util.Arrays;

import com.artemis.Component;

import unknow.kyhtanil.common.pojo.CharDesc;
import unknow.kyhtanil.common.pojo.UUID;

/**
 * @author unknow
 */
public class LogResult extends Component {
	/** the uuid */
	public UUID uuid = null;
	/** the charaters list */
	public CharDesc[] characters = null;

	/**
	 * create new LogResult
	 */
	public LogResult() {
	}

	/**
	 * create new LogResult
	 * 
	 * @param uuid
	 * @param characters
	 */
	public LogResult(UUID uuid, CharDesc[] characters) {
		this.uuid = uuid;
		this.characters = characters;
	}

	@Override
	public String toString() {
		return "LogResult [uuid=" + uuid + ", characters=" + Arrays.toString(characters) + "]";
	}
}