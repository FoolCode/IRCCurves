package curves.trigger.console;

import java.util.Hashtable;

import org.apache.log4j.Logger;

import curves.main.Bot;
import curves.message.IMessage;
import curves.trigger.IReadHandler;

public class R_Incoming implements IReadHandler {

	Logger log = Logger.getLogger(R_Incoming.class);
	
	public void process(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		log.debug("> " + message.getOriginal());
	}

	public boolean reactsTo(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		return true;
	}
	
	public boolean messageType(Class<IMessage> messageType) {
		return true;
	}

}
