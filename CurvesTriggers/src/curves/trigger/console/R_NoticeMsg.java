package curves.trigger.console;

import java.util.Hashtable;

import org.apache.log4j.Logger;

import curves.main.Bot;
import curves.message.IMessage;
import curves.message.NoticeMsg;
import curves.trigger.IReadHandler;

public class R_NoticeMsg implements IReadHandler {

	Logger log = Logger.getLogger(R_NoticeMsg.class);
	
	public void process(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		NoticeMsg noticemsg = ((NoticeMsg) message);
		log.info(noticemsg.getUser() + " has sent a notice to "
						+ noticemsg.getTarget() + " \""
						+ noticemsg.getMessage() + "\"");
	}

	public boolean reactsTo(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		return true;
	}
	
	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(NoticeMsg.class);
	}

}
