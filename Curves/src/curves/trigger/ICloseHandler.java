package curves.trigger;

import java.util.Hashtable;

import curves.main.Bot;

public interface ICloseHandler {

	public void process(Bot bot, Hashtable<String, Object> storage);
	
	public boolean reactsTo(Bot bot, Hashtable<String, Object> storage);
}
