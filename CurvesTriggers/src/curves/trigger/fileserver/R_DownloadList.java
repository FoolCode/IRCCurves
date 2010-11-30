package curves.trigger.fileserver;

import java.util.Hashtable;

import curves.main.Bot;
import curves.message.IMessage;
import curves.message.PrivMsg;
import curves.trigger.IReadHandler;

public class R_DownloadList implements IReadHandler {

	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(PrivMsg.class);
	}

	public void process(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		String text = (String) storage.get("list message");
		if (text == null) return;
		bot.send(new PrivMsg(message.getUser().getNickname(), text));
	}

	public boolean reactsTo(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		String msg = ((PrivMsg) message).getMessage();
		return msg.startsWith("!nyan") || msg.startsWith("!list")
				|| msg.startsWith("!help");
	}
}
