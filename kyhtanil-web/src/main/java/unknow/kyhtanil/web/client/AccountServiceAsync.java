package unknow.kyhtanil.web.client;

import com.google.gwt.user.client.rpc.*;

public interface AccountServiceAsync
	{
	void login(String login, String pass, AsyncCallback<Boolean> callback);

	void createAccount(String login, String pass, AsyncCallback<Boolean> callback);
	}
