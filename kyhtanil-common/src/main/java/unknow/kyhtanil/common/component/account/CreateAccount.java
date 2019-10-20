package unknow.kyhtanil.common.component.account;

import javax.annotation.Generated;

import com.artemis.Component;

@Generated(value="class unknow.pojo.Generator", date="2017-05-01T19:13:39+0200")
public class CreateAccount extends Component
	{
	public String login=null;
	public byte[] passHash=null;

	public CreateAccount()
		{
		}

	public CreateAccount(String login, byte[] passHash)
		{
		this.login=login;
		this.passHash=passHash;
		}

	public String toString()
		{
		return "login: "+login+", passHash: "+passHash;
		}
	}