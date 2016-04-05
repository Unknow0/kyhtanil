package unknow.kyhtanil.web.client;

/**
 * Interface to represent the messages contained in resource bundle:
 */
public interface Messages extends com.google.gwt.i18n.client.Messages
	{
	@DefaultMessage("Login")
	@Key("loginField")
	String loginField();

	@DefaultMessage("Password")
	@Key("passwordField")
	String passwordField();

	@DefaultMessage("Send")
	@Key("sendButton")
	String sendButton();
	}
