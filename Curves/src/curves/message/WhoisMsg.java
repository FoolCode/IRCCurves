package curves.message;

import curves.main.Profile;

/**
 * Class for WHOIS message (only outgoing)
 */
public class WhoisMsg extends IMessage {

	Profile profile;

	public Profile getProfile;

	public WhoisMsg(String message) {
		super(message);
		if (message == null)
			return;
	}

	public WhoisMsg(Profile profile) {
		super(null);
		this.profile = profile;
	}

	public String _toString() {
		return "WHOIS " + profile.getNickname();
	}

	public String getCommand() {
		return "WHOIS";
	}

}
