package unknow.kyhtanil.common.component.account;

import com.artemis.Component;

public class CreateAccount extends Component {
	public String login = null;
	public byte[] passHash = null;

	public CreateAccount() {
	}

	public CreateAccount(String login, byte[] passHash) {
		this.login = login;
		this.passHash = passHash;
	}

	public String toString() {
		return "login: " + login + ", passHash: " + passHash;
	}
}