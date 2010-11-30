package curves.message;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import curves.main.Channel;
import curves.main.Profile;

/**
 * Invitation message.
 */
public class InviteMsg extends IMessage {

	Channel channel;

	String target;

	public String getTarget() {
		return target;
	}

	public Channel getChannel() {
		return channel;
	}

	public InviteMsg(String message) {
		super(message);
		if (message == null) return;
		Pattern p = Pattern.compile("^:(.*?) INVITE (.*?) :(.*)$",
				Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(original);
		if (m.find()) {
			this.user = new Profile(m.group(1));
			this.target = m.group(2);
			this.channel = new Channel(m.group(3));
		}
	}

	public InviteMsg(Channel channel, String target) {
		super(null);
		this.target = target;
		this.channel = channel;
	}

	public String _toString() {
		return "INVITE " + target + " :" + channel;
	}

	public String getCommand() {
		return "INVITE";
	}

}
