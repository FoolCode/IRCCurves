package curves.trigger.foolrulez;

import java.util.Hashtable;

import curves.main.Bot;
import curves.message.IMessage;
import curves.message.PrivMsg;
import curves.trigger.IReadHandler;

public class R_Tits implements IReadHandler {

	public void process(IMessage message, Bot bot,
			Hashtable<String, Object> storage) {
		PrivMsg msg = (PrivMsg) message;
		msg.reply(bot, "(. Y .)");
	}

	public boolean reactsTo(IMessage message, Bot bot,
			Hashtable<String, Object> storage) {
		return
			 ((PrivMsg) message).getMessage().contains("tits or gtfo");
	}

	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(PrivMsg.class);
	}

}
