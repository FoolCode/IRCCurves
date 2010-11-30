package curves.trigger.record;

import java.util.Hashtable;

import curves.main.Bot;
import curves.main.Channel;
import curves.message.NamesMsg;
import curves.trigger.IPeriodicHandler;

/**
 * Updates the uptime table, works together with R_ReceiveNames and R_FinishNames
 */
public class P_QueryNames implements IPeriodicHandler {

	int skip = 0;
	
	public boolean isReady(Bot bot, Hashtable<String, Object> storage) {
		skip = (skip + 1) % 60;
		return skip == 0;
	}

	public void process(Bot bot, Hashtable<String, Object> storage) {
		for(Channel channel : bot.getProfile().getChannels()){
			bot.send(new NamesMsg(channel));
			storage.put("uptimes "+channel, 1);
		}	
	}

}
