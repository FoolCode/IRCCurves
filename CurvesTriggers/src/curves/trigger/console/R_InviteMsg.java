package curves.trigger.console;

import java.util.Hashtable;

import org.apache.log4j.Logger;

import curves.main.Bot;
import curves.message.IMessage;
import curves.message.InviteMsg;
import curves.trigger.IReadHandler;

public class R_InviteMsg implements IReadHandler {
	
	Logger log = Logger.getLogger(R_InviteMsg.class);

	public void process(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		InviteMsg invitemsg = (InviteMsg) message;
		log.info(invitemsg.getUser().getNickname() + " invited "
				+ invitemsg.getTarget() + " to " + invitemsg.getChannel());
	}

	public boolean reactsTo(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		return true;
	}

	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(InviteMsg.class);
	}
}
