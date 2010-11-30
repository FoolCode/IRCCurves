package curves.message;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import curves.main.Profile;

/**
 * class for the NICK command
 * IRC: NICK <nickname>
 */
public class NickMsg extends IMessage {
	
	Profile profile;

	public Profile getProfile() {
		return profile;
	}

	/**
	 * parse incoming NICK message
	 * @param message
	 */
	public NickMsg(String message) {
		super(message);
		if (message == null) return;
		Pattern p = Pattern.compile("^:(.*?) NICK :(.*)$",
				Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(original);
		if (m.find()) {
			this.user = new Profile(m.group(1));
			this.profile = new Profile(m.group(2), this.user.getHostname());
		}
	}

	/**
	 * create outgoing NICK message
	 * @param profile
	 */
	public NickMsg(Profile profile) {
		super(null);
		this.profile = profile;
	}

	public String _toString() {
		return "NICK " + profile.getNickname();
	}
	
	public String getCommand() {
		return "NICK";
	}

}
