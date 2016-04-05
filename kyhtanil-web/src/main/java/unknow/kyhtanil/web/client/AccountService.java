package unknow.kyhtanil.web.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("account")
public interface AccountService extends RemoteService
	{
	boolean login(String login, String pass) throws IllegalArgumentException;

	boolean createAccount(String login, String pass) throws IllegalArgumentException;
	}
