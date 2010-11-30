package curves.trigger.console;

import java.util.Hashtable;

import org.apache.log4j.Logger;

import curves.main.Bot;
import curves.message.IMessage;
import curves.message.JoinMsg;
import curves.trigger.IReadHandler;

public class R_JoinMsg implements IReadHandler {

	Logger log = Logger.getLogger(R_JoinMsg.class);
	
	public void process(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		JoinMsg joinmsg = ((JoinMsg) message);
		log.info(joinmsg.getUser() + " has joined "
				+ joinmsg.getChannel());
	}

	public boolean reactsTo(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		return true;
	}

	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(JoinMsg.class);
	}
}
