package curves.message;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import curves.main.Channel;
import curves.main.Profile;

/**
 * Class for 319 messages, only incoming Example: :eggbert.ca.us.irchighway.net
 * 319 curves curves :!@#foolrulez #guors
 */
public class WhoisChannelsMsg extends IMessage implements IWhoisReplyMsg{

	ArrayList<Channel> channels;
	
	Profile profile;
	
	public Profile getProfile(){
		return profile;
	}

	public ArrayList<Channel> getChannels() {
		return channels;
	}

	public WhoisChannelsMsg(String message) {
		super(message);
		channels = new ArrayList<Channel>();
		if (message == null)
			return;
		Pattern p = Pattern.compile("^:(.*?) 319 .*? (.*?) :(.*)$",
				Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(original);
		if (m.find()) {
			this.user = new Profile(m.group(1));
			this.profile = new Profile(m.group(2));
			String[] nameStrings = m.group(3).split(" ");
			for (String name : nameStrings) {
				Pattern pa = Pattern.compile("(.*?)(#.+)",
						Pattern.CASE_INSENSITIVE);
				Matcher ma = pa.matcher(name);
				if (ma.find()) {
					this.profile.addChannel(new Channel(ma.group(2)), Profile.parsePriv(ma.group(1)));
				}
			}
		}
	}

	public String _toString() {
		return null;
	}

	public String getCommand() {
		return "319";
	}

}
