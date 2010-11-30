package curves.trigger.console;

import java.util.Hashtable;

import org.apache.log4j.Logger;

import curves.main.Bot;
import curves.message.IMessage;
import curves.message.PrivMsg;
import curves.trigger.IReadHandler;

public class R_PrivMsg implements IReadHandler {

	Logger log = Logger.getLogger(R_PrivMsg.class);
	
	public void process(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		PrivMsg privmsg = ((PrivMsg) message);
		log.info("" + privmsg.getUser() + " says to "
				+ privmsg.getTarget() + " \"" + privmsg.getMessage() + "\"");
	}

	public boolean reactsTo(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		return true;
	}
	
	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(PrivMsg.class);
	}

}
