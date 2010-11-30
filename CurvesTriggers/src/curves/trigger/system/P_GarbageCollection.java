package curves.trigger.system;

import java.util.Hashtable;

import curves.main.Bot;
import curves.trigger.IPeriodicHandler;

public class P_GarbageCollection implements IPeriodicHandler {

	public boolean isReady(Bot bot, Hashtable<String, Object> storage) {
		return Math.random() > 0.99;
	}

	public void process(Bot bot, Hashtable<String, Object> storage) {
		System.gc();
	}

}
