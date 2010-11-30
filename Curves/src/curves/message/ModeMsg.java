package curves.message;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import curves.main.Channel;
import curves.main.Profile;

/**
 * Class for the MODE message which also include banning
 * IRC: MODE <channel> <mode> <target>
 */
public class ModeMsg extends IMessage {

	String target;
	String mode;
	Channel channel;

	public String getTarget() {
		return target;
	}

	public String getMode() {
		return mode;
	}

	public Channel getChannel() {
		return channel;
	}

	public ModeMsg(String message) {
		super(message);
		if (message == null)
			return;
		Pattern p = Pattern.compile("^:(.*?) MODE (#.*?) (.*?) (.*)$",
				Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(original);
		if (m.find()) {
			this.user = new Profile(m.group(1));
			this.channel = new Channel(m.group(2));
			this.target = m.group(4);
			this.mode = m.group(3);
		}
	}

	public ModeMsg(Channel channel, String mode, String target) {
		super(null);
		this.target = target;
		this.mode = mode;
		this.channel = channel;
	}

	@Override
	public String _toString() {
		return "MODE " + channel + " " + mode + " " + target;
	}

	public String getCommand() {
		return "MODE";
	}

}
