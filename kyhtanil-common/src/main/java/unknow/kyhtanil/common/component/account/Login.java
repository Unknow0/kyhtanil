package unknow.kyhtanil.common.component.account;

import com.artemis.Component;

public class Login extends Component {
	public String login = null;
	public byte[] passHash = null;

	public Login() {
	}

	public Login(String login, byte[] passHash) {
		this.login = login;
		this.passHash = passHash;
	}

	@Override
	public String toString() {
		return "Login [login=" + login + "]";
	}
}