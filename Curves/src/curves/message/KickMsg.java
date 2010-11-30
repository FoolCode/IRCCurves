package curves.message;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import curves.main.Channel;
import curves.main.Profile;

/**
 * Kick message.
 */
public class KickMsg extends IMessage {

	private Profile target;
	private Channel channel;

	private String message;

	public String getMessage() {
		return message;
	}

	public Channel getChannel() {
		return channel;
	}

	public Profile getTarget() {
		return target;
	}

	public KickMsg(String message) {
		super(message);
		if (message == null)
			return;
		Pattern p = Pattern.compile("^:(.*?) KICK (#.*?) (.*?) :(.*)$",
				Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(original);
		if (m.find()) {
			this.user = new Profile(m.group(1));
			this.channel = new Channel(m.group(2));
			this.target = new Profile(m.group(3));
			this.message = m.group(4);
			if (this.message.equals(this.user.getNickname()))
				this.message = null;
		}
	}

	public KickMsg(Channel channel, String message, Profile target) {
		super(null);
		this.target = target;
		this.message = message;
		this.channel = channel;
	}

	public String _toString() {
		return "KICK " + channel + " " + target.getNickname()
				+ ((message == null) ? "" : (" :" + message));
	}

	public String getCommand() {
		return "KICK";
	}

}
