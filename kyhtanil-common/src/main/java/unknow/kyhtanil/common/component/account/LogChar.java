package unknow.kyhtanil.common.component.account;

import com.artemis.Component;

import unknow.kyhtanil.common.pojo.UUID;

/**
 * @author unknow
 */
public class LogChar extends Component {
	/** the uuid */
	public UUID uuid = null;
	/** the char id to log */
	public int character = 0;

	/**
	 * create new LogChar
	 */
	public LogChar() {
	}

	/**
	 * create new LogChar
	 * 
	 * @param uuid
	 * @param character
	 */
	public LogChar(UUID uuid, int character) {
		this.uuid = uuid;
		this.character = character;
	}

	@Override
	public String toString() {
		return "LogChar [uuid=" + uuid + ", character=" + character + "]";
	}

}