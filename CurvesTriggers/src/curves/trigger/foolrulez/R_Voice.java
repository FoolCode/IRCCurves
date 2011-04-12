package curves.trigger.foolrulez;

import java.util.Hashtable;

import curves.main.Bot;
import curves.message.IMessage;
import curves.message.ModeMsg;
import curves.message.PrivMsg;
import curves.trigger.IReadHandler;
import curves.trigger.lib.MainChannel;

public class R_Voice implements IReadHandler {

	public void process(IMessage message, Bot bot,
			Hashtable<String, Object> storage) {
		PrivMsg msg = (PrivMsg) message;
		bot.send(new ModeMsg(MainChannel.get(storage), "+v", msg.getUser()
				.getNickname()));
	}

	public boolean reactsTo(IMessage message, Bot bot,
			Hashtable<String, Object> storage) {
		return MainChannel.in(message, storage)
				&& ((PrivMsg) message).getMessage().contains("voice");
	}

	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(PrivMsg.class);
	}

}
