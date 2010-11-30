package curves.message;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import curves.main.Profile;

/**
 * Class for 319 messages, only incoming Example: :eggbert.ca.us.irchighway.net
 * 319 curves curves :!@#foolrulez #guors
 */
public class WhoisNameMsg extends IMessage implements IWhoisReplyMsg{

	Profile profile;
	
	public Profile getProfile(){
		return profile;
	}

	public WhoisNameMsg(String message) {
		super(message);
		if (message == null)
			return;
		Pattern p = Pattern.compile("^:(.*?) 311 .*? (.*?) (.*?) (.*?) * :(.*)$",
				Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(original);
		if (m.find()) {
			this.user = new Profile(m.group(1));
			this.profile = new Profile(m.group(2), m.group(3)+"@"+m.group(4));
			this.profile.setRealName(m.group(5));
		}
	}

	public String _toString() {
		return null;
	}

	public String getCommand() {
		return "311";
	}

}
