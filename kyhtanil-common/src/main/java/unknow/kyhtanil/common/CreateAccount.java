/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package unknow.kyhtanil.common;

import unknow.common.tools.*;

import com.artemis.*;

public class CreateAccount extends PooledComponent
	{
	public String login;
	public byte[] passHash;

	/**
	 * Default constructor.
	 */
	public CreateAccount()
		{
		}

	/**
	 * All-args constructor.
	 */
	public CreateAccount(String login, byte[] passHash)
		{
		this.login=login;
		this.passHash=passHash;
		}

	protected void reset()
		{
		}

	public String toString()
		{
		return JsonUtils.toString(this);
		}
	}
