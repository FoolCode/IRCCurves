package curves.trigger.console;

import java.util.Hashtable;

import org.apache.log4j.Logger;

import curves.main.Bot;
import curves.message.IMessage;
import curves.message.PartMsg;
import curves.trigger.IReadHandler;

public class R_PartMsg implements IReadHandler {

	Logger log = Logger.getLogger(R_PartMsg.class);
	
	public void process(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		PartMsg partmsg = ((PartMsg) message);
		log.info(partmsg.getUser() + " has left "
				+ partmsg.getChannel());
	}

	public boolean reactsTo(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		return true;
	}

	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(PartMsg.class);
	}
}
