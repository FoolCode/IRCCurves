package curves.trigger.console;

import java.util.Date;
import java.util.Hashtable;

import org.apache.log4j.Logger;

import curves.main.Bot;
import curves.trigger.IPeriodicHandler;

public class P_Time implements IPeriodicHandler {

	Logger log = Logger.getLogger(P_Time.class);
	
	int skip = 0;

	public boolean isReady(Bot bot, Hashtable<String, Object> storage) {
		skip = (int) ((skip + 1) % (60000 / bot.getPeriodicTimer()));
		return skip == 0;
	}

	public void process(Bot bot, Hashtable<String, Object> storage) {
		log.info(new Date());
	}

}
