package unknow.kyhtanil.common.component.net;

import java.util.Arrays;

import com.artemis.Component;

import unknow.kyhtanil.common.pojo.UUID;

/**
 * update info event
 * 
 * @author unknow
 */
public class UpdateInfo extends Component {
	/** the uuid to update */
	public UUID uuid = null;
	/** the component t update */
	public Component[] c;

	/**
	 * create new UpdateInfo
	 */
	public UpdateInfo() {
	}

	/**
	 * create new UpdateInfo
	 * 
	 * @param uuid
	 * @param c
	 */
	public UpdateInfo(UUID uuid, Component... c) {
		this.uuid = uuid;
		this.c = c;
	}

	@Override
	public String toString() {
		return "UpdateInfo [uuid=" + uuid + ", c=" + Arrays.toString(c) + "]";
	}
}
