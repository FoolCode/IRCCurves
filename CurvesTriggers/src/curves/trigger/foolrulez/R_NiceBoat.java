package curves.trigger.foolrulez;

import java.util.Hashtable;

import curves.main.Bot;
import curves.main.Channel;
import curves.main.Profile;
import curves.message.IMessage;
import curves.message.KickMsg;
import curves.message.PrivMsg;
import curves.trigger.IReadHandler;
import curves.trigger.lib.MainChannel;

public class R_NiceBoat implements IReadHandler {

	public void process(IMessage message, Bot bot,
			Hashtable<String, Object> storage) {
		if (storage.containsKey("niceboat user")) {
			Profile niceboat = (Profile) storage.get("niceboat user");
			Channel channel = MainChannel.get(storage);
			String reply = ((PrivMsg) message).trailing();
			if (reply.equals(""))
				reply = (String) storage.get("niceboat message");
			if (reply == null)
				reply = "";
			bot.send(new KickMsg(channel, reply, niceboat));
		}
		storage.put("niceboat user", message.getUser());
	}

	public boolean reactsTo(IMessage message, Bot bot,
			Hashtable<String, Object> storage) {
		return ((PrivMsg) message).getMessage().startsWith("!niceboat")
				&& MainChannel.in(message, storage);
	}

	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(PrivMsg.class);
	}

}
