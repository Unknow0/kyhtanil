package unknow.kyhtanil.common.component.account;

import com.artemis.Component;

/**
 * @author unknow
 */
public class CreateAccount extends Component {
	/** the login */
	public String login = null;
	/** the pass */
	public byte[] passHash = null;

	/**
	 * create new CreateAccount
	 */
	public CreateAccount() {
	}

	/**
	 * create new CreateAccount
	 * 
	 * @param login
	 * @param passHash
	 */
	public CreateAccount(String login, byte[] passHash) {
		this.login = login;
		this.passHash = passHash;
	}

	@Override
	public String toString() {
		return "CreateAccount [login=" + login + "]";
	}
}