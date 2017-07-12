package unknow.kyhtanil.common.component.account;

import com.artemis.PooledComponent;
import javax.annotation.Generated;

@Generated(value="class unknow.pojo.Generator", date="2017-05-01T19:13:39+0200")
public class CreateAccount extends PooledComponent {
	public String login=null;
	public byte[] passHash=null;

	public CreateAccount() {
	}

	public CreateAccount(String login, byte[] passHash) {
		this.login=login;
		this.passHash=passHash;
	}

	public void reset() {
		login=null;
		passHash=null;
	}

	public String toString() {
		return "login: "+login+", passHash: "+passHash;
	}
}