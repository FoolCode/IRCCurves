package curves.trigger.console;

import java.util.Hashtable;

import org.apache.log4j.Logger;

import curves.main.Bot;
import curves.message.IMessage;
import curves.message.KickMsg;
import curves.trigger.IReadHandler;

public class R_KickMsg implements IReadHandler {

	Logger log = Logger.getLogger(R_KickMsg.class);
	
	public void process(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		KickMsg kickmsg = ((KickMsg) message);
		log.info(kickmsg.getTarget() + " has been kicked by "
				+ kickmsg.getUser() + " from " + kickmsg.getChannel()
				+ ". The reason is\"" + kickmsg.getMessage() + "\"");
	}

	public boolean reactsTo(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		return true;
	}
	
	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(KickMsg.class);
	}

}
