package curves.message;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import curves.main.Channel;
import curves.main.Profile;

/**
 * 353 messages from the server indicate incoming list of names on a channel.
 * You don't send out any 353 messages by yourself!
 */
public class NamesReplyMsg extends IMessage {

	Channel channel;
	String target;
	ArrayList<Profile> names;

	public ArrayList<Profile> getNames() {
		return names;
	}

	public Channel getChannel() {
		return channel;
	}

	public String getTarget() {
		return target;
	}

	public NamesReplyMsg(String message) {
		super(message);
		names = new ArrayList<Profile>();
		if (message == null)
			return;
		Pattern p = Pattern.compile("^:(.*?) 353 (.*?) = (#.*?) :(.*)$",
				Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(original);
		if (m.find()) {
			this.user = new Profile(m.group(1));
			this.channel = new Channel(m.group(3));
			this.target = m.group(2);
			String[] nameStrings = m.group(4).split(" ");
			for (String name : nameStrings) {
				Pattern pa = Pattern.compile("(.*?)([^@%\\+!#&]+)$",
						Pattern.CASE_INSENSITIVE);
				Matcher ma = pa.matcher(name);
				if (ma.find()) {
					Profile user = new Profile(ma.group(2));
					user.addChannel(channel, Profile.parsePriv(ma.group(1)));
					names.add(user);
				}
			}
		}

	}

	public String getCommand() {
		return "353";
	}

	public String _toString() {
		return "";
	}

}
