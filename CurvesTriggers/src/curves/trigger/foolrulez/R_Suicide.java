package curves.trigger.foolrulez;

import java.util.Hashtable;

import curves.main.Bot;
import curves.main.Channel;
import curves.message.IMessage;
import curves.message.KickMsg;
import curves.message.PrivMsg;
import curves.trigger.IReadHandler;
import curves.trigger.lib.MainChannel;

public class R_Suicide implements IReadHandler {

	public void process(IMessage message, Bot bot,
			Hashtable<String, Object> storage) {
			PrivMsg msg = (PrivMsg) message;
			bot.send(new KickMsg(new Channel(msg.getTarget()), "okeydokey", msg.getUser()));
	}

	public boolean reactsTo(IMessage message, Bot bot,
			Hashtable<String, Object> storage) {
		String msg = ((PrivMsg) message).getMessage();
		return MainChannel.in(message, storage) &&
		(
				msg.startsWith("!killme")
			 || msg.startsWith("!kickme")
			 || msg.startsWith("!suicide")
			 || msg.startsWith("!seppuku")
			 || msg.startsWith("!cyanide")
		);
	}

	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(PrivMsg.class);
	}

}
