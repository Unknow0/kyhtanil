package unknow.kyhtanil.common.component.account;

import com.artemis.Component;

import unknow.kyhtanil.common.pojo.UUID;

/**
 * @author unknow
 */
public class CreateChar extends Component {
	/** the uuid */
	public UUID uuid;
	/** the name of the new char */
	public String name;

	/**
	 * create new CreateChar
	 */
	public CreateChar() {
	}

	/**
	 * create new CreateChar
	 * 
	 * @param uuid
	 * @param name
	 */
	public CreateChar(UUID uuid, String name) {
		this.uuid = uuid;
		this.name = name;
	}

	@Override
	public String toString() {
		return "CreateChar [uuid=" + uuid + ", name=" + name + "]";
	}
}