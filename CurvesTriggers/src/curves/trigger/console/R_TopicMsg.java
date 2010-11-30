package curves.trigger.console;

import java.util.Hashtable;

import org.apache.log4j.Logger;

import curves.main.Bot;
import curves.message.IMessage;
import curves.message.TopicMsg;
import curves.trigger.IReadHandler;

public class R_TopicMsg implements IReadHandler {

	Logger log = Logger.getLogger(R_TopicMsg.class);
	
	public void process(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		TopicMsg topicmsg = (TopicMsg) message;
		log.info(topicmsg.getUser().getNickname()
				+ " changed the topic of " + topicmsg.getChannel() + " to \""
				+ topicmsg.getTopic() + "\"");
	}

	public boolean reactsTo(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		return true;
	}
	
	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(TopicMsg.class);
	}

}
