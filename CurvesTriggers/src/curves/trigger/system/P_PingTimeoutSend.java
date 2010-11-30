package curves.trigger.system;

import java.util.Hashtable;

import org.apache.log4j.Logger;

import curves.main.Bot;
import curves.message.PingMsg;
import curves.trigger.IPeriodicHandler;

/**
 * checks for ping timeout in combination with R_Pong: this class sends out
 * pings in fixed time intervals and checks for receive timestamp that should be
 * set by R_Pong
 */
public class P_PingTimeoutSend implements IPeriodicHandler {

	int skip = 0;
	
	Logger log = Logger.getLogger(P_PingTimeoutSend.class);

	public boolean isReady(Bot bot, Hashtable<String, Object> storage) {
		skip = (skip + 1) % 6;
		return skip == 0;
	}

	public void process(Bot bot, Hashtable<String, Object> storage) {
		if (storage.containsKey("pingtimer")) {
			long timegap = (System.currentTimeMillis() - ((Long) storage
					.get("pingtimer")).longValue());
			int timeout = Integer.parseInt((String) storage.get("ping timeout"));
			if (timegap > timeout) {
				timegap = timegap / 1000;
				log.error("Ping timeout (" + timegap + " seconds)");
				storage.remove("pingtimer");
				bot.close();
				return;
			}
		} else {
			// first time using it
			storage.put("pingtimer", new Long(System.currentTimeMillis()));
		}

		bot.send(new PingMsg("PingServ", false));
	}
}
