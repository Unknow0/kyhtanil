package unknow.kyhtanil.server.pojo;

public class Account
	{
	private int id;
	private String login;
	private byte[] passHash;

	public Account(String login, byte[] passHash)
		{
		this.login=login;
		this.passHash=passHash;
		}

	public int getId()
		{
		return id;
		}

	public String getLogin()
		{
		return login;
		}

	public byte[] getPassHash()
		{
		return passHash;
		}
	}
