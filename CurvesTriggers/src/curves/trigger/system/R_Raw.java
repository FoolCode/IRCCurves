package curves.trigger.system;

import java.util.Hashtable;

import curves.main.Bot;
import curves.message.Generic;
import curves.message.IMessage;
import curves.message.NoticeMsg;
import curves.trigger.IReadHandler;

public class R_Raw implements IReadHandler {

	public void process(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		bot.send(new Generic(((NoticeMsg) message).getMessage().substring(
				"raw ".length())));
	}

	public boolean reactsTo(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		return message.isFromAdminOf(bot)
				&& ((NoticeMsg) message).getMessage().startsWith("raw ");
	}
	
	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(NoticeMsg.class);
	}

}
