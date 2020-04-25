package unknow.kyhtanil.common.component.account;

import com.artemis.Component;

import unknow.kyhtanil.common.pojo.UUID;

public class CreateChar extends Component {
	public UUID uuid;
	public String name;

	public CreateChar() {
	}

	public CreateChar(UUID uuid, String name) {
		this.uuid = uuid;
		this.name = name;
	}

	@Override
	public String toString() {
		return "CreateChar [uuid=" + uuid + ", name=" + name + "]";
	}
}