package curves.message;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import curves.main.Channel;
import curves.main.Profile;

/**
 * 353 messages from the server indicate the end of a list of names on a
 * channel. You don't send out any 353 messages by yourself!
 */
public class NamesEndMsg extends IMessage {

	Channel channel;
	String target;
	
	public String getTarget(){
		return target;
	}
	
	public Channel getChannel(){
		return channel;
	}

	public NamesEndMsg(String message) {
		super(message);
		if (message == null)
			return;
		Pattern p = Pattern.compile("^:(.*?) 366 (.*?) (#.*?) :End",
				Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(original);
		if (m.find()) {
			this.user = new Profile(m.group(1));
			this.channel = new Channel(m.group(3));
			this.target = m.group(2);
		}
	}

	public String getCommand() {
		return "366";
	}

	public String _toString() {
		return "";
	}

}
