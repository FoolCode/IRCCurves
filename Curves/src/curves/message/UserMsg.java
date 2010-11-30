package curves.message;

import curves.main.Profile;

/**
 * Class for the USER command.
 * IRC: USER <nick> <hostname> <servername>: <realname>
 */
public class UserMsg extends IMessage {

	Profile profile;

	public Profile getProfile() {
		return profile;
	}

	/**
	 * There are no incoming USER messages.
	 * @param message
	 */
	public UserMsg(String message) {
		super(message);
	}

	/**
	 * create outgoing USER message;
	 * 
	 * @param profile
	 */
	public UserMsg(Profile profile) {
		super(null);
		this.profile = profile;
	}

	public String _toString() {
		return "USER " + profile.getNickname() + " " + profile.getHostname()
				+ " " + profile.getServername() + " :" + profile.getRealname();
	}
	
	public String getCommand() {
		return "USER";
	}

}
