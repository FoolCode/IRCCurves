package curves.trigger.mana;

import java.util.Hashtable;

import org.apache.log4j.Logger;

import curves.main.Bot;
import curves.message.IMessage;
import curves.message.JoinMsg;
import curves.trigger.IReadHandler;
import curves.trigger.lib.MainChannel;

public class R_Join implements IReadHandler {

	Logger log = Logger.getLogger(R_Join.class);

	public void process(IMessage message, Bot bot,
			Hashtable<String, Object> storage) {
		LibMana.newPlayer(message.getUser(), bot);
	}

	public boolean reactsTo(IMessage message, Bot bot,
			Hashtable<String, Object> storage) {
		return MainChannel.in(message, storage);
	}

	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(JoinMsg.class);
	}

}
