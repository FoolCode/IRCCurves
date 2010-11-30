package curves.message;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import curves.main.Channel;
import curves.main.Profile;

public class TopicMsg extends IMessage {

	Channel channel;

	String topic;

	public Channel getChannel() {
		return channel;
	}

	public String getTopic() {
		return topic;
	}

	public TopicMsg(String message) {
		super(message);
		if (message == null)
			return;
		Pattern p = Pattern.compile("^:(.*?) TOPIC (.*?) :(.*)$",
				Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(original);
		if (m.find()) {
			this.user = new Profile(m.group(1));
			this.channel = new Channel(m.group(2));
			this.topic = m.group(3);
		}
	}
	
	public TopicMsg(Channel channel, String topic){
		super(null);
		this.channel = channel;
		this.topic = topic;
	}

	public String _toString() {
		return "TOPIC " + channel + ((topic == null)?"":(" :" + topic));
	}

	public String getCommand() {
		return "TOPIC";
	}

}
