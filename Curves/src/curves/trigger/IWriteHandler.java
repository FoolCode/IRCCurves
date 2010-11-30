package curves.trigger;

import java.util.Hashtable;

import curves.main.Bot;
import curves.message.IMessage;

public interface IWriteHandler {

	public boolean reactsTo(IMessage message, Bot bot, Hashtable<String, Object> storage);
	
	public boolean messageType(Class<IMessage> messageType);
	
	public void process(IMessage message, Bot bot, Hashtable<String, Object> storage);
}
