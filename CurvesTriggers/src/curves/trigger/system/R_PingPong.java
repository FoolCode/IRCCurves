package curves.trigger.system;

import java.util.Hashtable;

import curves.main.Bot;
import curves.message.IMessage;
import curves.message.PingMsg;
import curves.message.PongMsg;
import curves.trigger.IReadHandler;

public class R_PingPong implements IReadHandler {

	public void process(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		PingMsg ping = (PingMsg) message;
		bot.send(new PongMsg(ping.getTarget(), false));
	}

	public boolean reactsTo(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		return true;
	}
	
	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(PingMsg.class);
	}

}
