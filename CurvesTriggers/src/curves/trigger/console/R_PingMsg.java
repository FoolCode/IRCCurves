package curves.trigger.console;

import java.util.Hashtable;

import org.apache.log4j.Logger;

import curves.main.Bot;
import curves.message.IMessage;
import curves.message.PingMsg;
import curves.trigger.IReadHandler;

public class R_PingMsg implements IReadHandler {

	Logger log = Logger.getLogger(R_PingMsg.class);
	
	public void process(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		log.info("Ping request: "+((PingMsg) message).getTarget());
	}

	public boolean reactsTo(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		return true;
	}

	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(PingMsg.class);
	}
}
