package curves.trigger.console;

import java.util.Hashtable;

import org.apache.log4j.Logger;

import curves.main.Bot;
import curves.message.ErrorMsg;
import curves.message.IMessage;
import curves.trigger.IReadHandler;

public class R_RegisterError implements IReadHandler {

	Logger log = Logger.getLogger(R_RegisterError.class);
	
	public void process(IMessage message, Bot bot,
			Hashtable<String, Object> storage) {
		log.warn("IRC Error Message: " + ((ErrorMsg) message).getError());
	}

	public boolean reactsTo(IMessage message, Bot bot,
			Hashtable<String, Object> storage) {
		return true;
	}

	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(ErrorMsg.class);
	}

}
