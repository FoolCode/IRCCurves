package curves.message;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import curves.main.Channel;
import curves.main.Profile;

/**
 * Join message.
 */
public class JoinMsg extends IMessage {

	Channel channel;

	public Channel getChannel() {
		return channel;
	}

	public JoinMsg(String message) {
		super(message);
		if (message == null) return;
		Pattern p = Pattern.compile("^:(.*?) JOIN :(.*)$", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(original);
		if (m.find()) {
			String[] userinfo = m.group(1).split("!");
			this.user = new Profile(userinfo[0], userinfo[1]);
			this.channel = new Channel(m.group(2));
		}
	}

	public JoinMsg(Channel channel) {
		super(null);
		this.channel = channel;
	}

	public String _toString() {
		return "JOIN " + channel;
	}

	public String getCommand() {
		return "JOIN";
	}

}
