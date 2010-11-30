package curves.trigger.record;

import java.util.Hashtable;

import curves.main.Bot;
import curves.message.IMessage;
import curves.message.NamesEndMsg;
import curves.trigger.IReadHandler;

/**
 * Updates the uptime table, works together with P_QueryNames and R_ReceiveNames
 */
public class R_FinishNames implements IReadHandler {

	public void process(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		storage.remove("uptimes " + ((NamesEndMsg) message).getChannel());
	}

	public boolean reactsTo(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		return storage.containsKey(
						"uptimes " + ((NamesEndMsg) message).getChannel());
	}
	
	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(NamesEndMsg.class);
	}

}
