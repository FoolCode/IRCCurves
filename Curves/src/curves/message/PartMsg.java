package curves.message;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import curves.main.Channel;
import curves.main.Profile;

/**
 * Part message.
 */
public class PartMsg extends IMessage {

	Channel channel;

	public Channel getChannel() {
		return channel;
	}

	public PartMsg(String message) {
		// TODO read content
		super(message);
		if (message == null)
			return;
		Pattern p = Pattern.compile("^:(.*?) PART (.*)$",
				Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(original);
		if (m.find()) {
			String[] userinfo = m.group(1).split("!");
			this.user = new Profile(userinfo[0], userinfo[1]);
			this.channel = new Channel(m.group(2));
		}
	}

	public PartMsg(Channel channel) {
		super(null);
		this.channel = channel;
	}

	public String _toString() {
		return "PART " + channel;
	}

	public String getCommand() {
		return "PART";
	}

}
