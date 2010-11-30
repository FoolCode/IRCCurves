package curves.trigger.system;

import java.util.Hashtable;

import curves.main.Bot;
import curves.message.IMessage;
import curves.message.PongMsg;
import curves.trigger.IReadHandler;

public class R_PingTimeoutReceive implements IReadHandler {

	public void process(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		storage.put("pingtimer", new Long(System.currentTimeMillis()));
	}

	public boolean reactsTo(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		return true;
	}
	
	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(PongMsg.class);
	}

}
