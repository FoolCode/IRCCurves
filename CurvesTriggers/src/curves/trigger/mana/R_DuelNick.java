package curves.trigger.mana;

import java.util.Hashtable;

import curves.main.Bot;
import curves.main.Profile;
import curves.message.IMessage;
import curves.message.NickMsg;
import curves.trigger.IReadHandler;

/**
 * In case the duelist changes his nick
 */
public class R_DuelNick implements IReadHandler {

	public void process(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		storage.put("duel user", ((NickMsg) message).getProfile());
	}

	public boolean reactsTo(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		return message.getUser().equals(
						(Profile) storage.get("duel user"));
	}

	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(NickMsg.class);
	}
}
