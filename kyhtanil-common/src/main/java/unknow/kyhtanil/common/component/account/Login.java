package unknow.kyhtanil.common.component.account;

import com.artemis.Component;

/**
 * @author unknow
 */
public class Login extends Component {
	/** the login */
	public String login = null;
	/** the pass */
	public byte[] passHash = null;

	/**
	 * create new Login
	 */
	public Login() {
	}

	/**
	 * create new Login
	 * 
	 * @param login
	 * @param passHash
	 */
	public Login(String login, byte[] passHash) {
		this.login = login;
		this.passHash = passHash;
	}

	@Override
	public String toString() {
		return "Login [login=" + login + "]";
	}
}