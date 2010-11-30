package curves.trigger;

import java.util.Hashtable;

import curves.main.Bot;

public interface IPeriodicHandler {
	
	abstract public void process(Bot bot, Hashtable<String, Object> storage);
	
	abstract public boolean isReady(Bot bot, Hashtable<String, Object> storage);
}
