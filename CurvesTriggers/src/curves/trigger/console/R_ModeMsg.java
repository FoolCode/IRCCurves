package curves.trigger.console;

import java.util.Hashtable;

import org.apache.log4j.Logger;

import curves.main.Bot;
import curves.message.IMessage;
import curves.message.ModeMsg;
import curves.trigger.IReadHandler;

public class R_ModeMsg implements IReadHandler {

	Logger log = Logger.getLogger(R_ModeMsg.class);
	
	public void process(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		ModeMsg modemsg = ((ModeMsg) message);
		log.info(modemsg.getUser() + " has set " + modemsg.getMode()
						+ " for " + modemsg.getTarget() + " on "
						+ modemsg.getChannel());
	}

	public boolean reactsTo(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		return true;
	}
	
	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(ModeMsg.class);
	}

}
