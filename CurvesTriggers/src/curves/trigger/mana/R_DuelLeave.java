package curves.trigger.mana;

import java.util.Hashtable;

import curves.main.Bot;
import curves.main.Profile;
import curves.message.IMessage;
import curves.message.KickMsg;
import curves.message.PartMsg;
import curves.message.QuitMsg;
import curves.trigger.IReadHandler;
import curves.trigger.lib.MainChannel;

/**
 * In case somebody leaves before he can duel.
 */
public class R_DuelLeave implements IReadHandler {

	public void process(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		storage.remove("duel user");
	}

	public boolean reactsTo(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		if (!storage.containsKey("duel user")) {
			return false;
		} else {
			Profile duelUser = (Profile) storage.get("duel user");
			return (message instanceof QuitMsg && message.getUser().equals(
					duelUser))
					|| (message instanceof PartMsg
							&& message.getUser().equals(duelUser) && MainChannel.in(message, storage))
					|| (message instanceof KickMsg
							&& ((KickMsg) message).getTarget().equals(duelUser) && MainChannel.in(message, storage));
		}
	}

	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(KickMsg.class)
				|| messageType.equals(PartMsg.class)
				|| messageType.equals(QuitMsg.class);
	}
}
