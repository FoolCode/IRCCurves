package curves.trigger.console;

import java.util.Hashtable;

import org.apache.log4j.Logger;

import curves.main.Bot;
import curves.message.IMessage;
import curves.trigger.IWriteHandler;

public class W_Outgoing implements IWriteHandler {

	Logger log = Logger.getLogger(W_Outgoing.class);
	
	public void process(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		log.debug("< " + message);
	}

	public boolean reactsTo(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		return true;
	}
	
	public boolean messageType(Class<IMessage> messageType) {
		return true;
	}

}
