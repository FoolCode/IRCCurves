package curves.trigger.system;

import java.util.Hashtable;

import curves.main.Bot;
import curves.message.WhoisMsg;
import curves.trigger.IPeriodicHandler;

/**
 * Keeps the channels list of the bot up-to-date, works together with
 * R_ReceiveChannels
 * 
 * @author guo
 * 
 */
public class P_QueryChannels implements IPeriodicHandler {

	int skip = 0;

	public boolean isReady(Bot bot, Hashtable<String, Object> storage) {
		skip = (skip + 1) % 60;
		return skip == 4 && !storage.containsKey("nick taken");
	}

	public void process(Bot bot, Hashtable<String, Object> storage) {
		bot.send(new WhoisMsg(bot.getProfile()));
	}

}
