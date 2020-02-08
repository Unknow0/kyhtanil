package unknow.kyhtanil.common.component.net;

import java.util.Arrays;

import com.artemis.Component;

import unknow.kyhtanil.common.pojo.UUID;

public class UpdateInfo extends Component {
	public UUID uuid = null;
	public Component[] c;

	public UpdateInfo() {
	}

	public UpdateInfo(UUID uuid, Component[] c) {
		this.uuid = uuid;
		this.c = c;
	}

	public String toString() {
		return "uuid: " + uuid + ", changed: " + Arrays.toString(c);
	}
}
