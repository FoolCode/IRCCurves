package curves.trigger.console;

import java.util.Hashtable;

import org.apache.log4j.Logger;

import curves.main.Bot;
import curves.message.IMessage;
import curves.message.NickMsg;
import curves.trigger.IReadHandler;

public class R_NickMsg implements IReadHandler {

	Logger log = Logger.getLogger(R_NickMsg.class);
	
	public void process(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		log.info(message.getUser().getNickname()
				+ " changed his nick to "
				+ ((NickMsg) message).getProfile().getNickname());
	}

	public boolean reactsTo(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		return true;
	}
	
	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(NickMsg.class);
	}

}
