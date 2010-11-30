package curves.trigger.foolrulez;

import java.util.Hashtable;

import curves.main.Bot;
import curves.main.Profile;
import curves.message.IMessage;
import curves.message.NickMsg;
import curves.trigger.IReadHandler;

public class R_NiceBoatNick implements IReadHandler {

	public void process(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		storage.put("niceboat user", ((NickMsg) message).getProfile());
	}

	public boolean reactsTo(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		return message.getUser().equals(
						(Profile) storage.get("niceboat user"));
	}

	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(NickMsg.class);
	}

}
