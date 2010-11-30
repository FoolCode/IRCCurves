package curves.trigger.lib;

import java.util.Hashtable;

import curves.main.Channel;
import curves.message.IMessage;
import curves.message.JoinMsg;
import curves.message.KickMsg;
import curves.message.PartMsg;
import curves.message.PrivMsg;

public abstract class MainChannel {
	
	public static boolean in(IMessage message,
			Hashtable<String, Object> storage) {
		if (!storage.containsKey("main channel")) return true;
		if (message instanceof PrivMsg)
			return ((PrivMsg) message).getTarget().equals(
					(String) storage.get("main channel"));
		if (message instanceof JoinMsg)
			return ((JoinMsg) message).getChannel().getChannel().equals(
					(String) storage.get("main channel"));
		if (message instanceof PartMsg)
			return ((PartMsg) message).getChannel().getChannel().equals(
					(String) storage.get("main channel"));
		if (message instanceof KickMsg)
			return ((KickMsg) message).getChannel().getChannel().equals(
					(String) storage.get("main channel"));
		return false;
	}

	public static Channel get(Hashtable<String, Object> storage) {
		return new Channel((String) storage.get("main channel"));
	}
	
}
