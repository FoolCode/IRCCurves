package curves.message;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import curves.main.Profile;

/**
 * Class for 319 messages, only incoming Example: :eggbert.ca.us.irchighway.net
 * 319 curves curves :!@#foolrulez #guors
 */
public class WhoisEndMsg extends IMessage implements IWhoisReplyMsg{

	Profile profile;
	
	public Profile getProfile(){
		return profile;
	}

	public WhoisEndMsg(String message) {
		super(message);
		if (message == null)
			return;
		Pattern p = Pattern.compile("^:(.*?) 318 .*? (.*?) :End of /WHOIS.*$",
				Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(original);
		if (m.find()) {
			this.user = new Profile(m.group(1));
			this.profile = new Profile(m.group(2));
		}
	}

	public String _toString() {
		return null;
	}

	public String getCommand() {
		return "318";
	}

}
