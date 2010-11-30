package curves.trigger.foolrulez;

import java.util.Hashtable;

import curves.main.Bot;
import curves.main.Profile;
import curves.message.IMessage;
import curves.message.PartMsg;
import curves.message.QuitMsg;
import curves.trigger.IReadHandler;
import curves.trigger.lib.MainChannel;

public class R_NiceBoatLeave implements IReadHandler {

	public void process(IMessage message, Bot bot,
			Hashtable<String, Object> storage) {
		storage.remove("niceboat user");
	}

	public boolean reactsTo(IMessage message, Bot bot,
			Hashtable<String, Object> storage) {
		if (storage.containsKey("niceboat user")) {
			return false;
		} else {
			Profile duelUser = (Profile) storage.get("niceboat user");
			return message.getUser().equals(duelUser)
					|| (message.getUser().equals(duelUser) && MainChannel.in(
							message, storage));
		}
	}

	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(QuitMsg.class)
				|| messageType.equals(PartMsg.class);
	}

}
