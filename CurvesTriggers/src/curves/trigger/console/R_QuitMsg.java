package curves.trigger.console;

import java.util.Hashtable;

import org.apache.log4j.Logger;

import curves.main.Bot;
import curves.message.IMessage;
import curves.message.QuitMsg;
import curves.trigger.IReadHandler;

public class R_QuitMsg implements IReadHandler {

	Logger log = Logger.getLogger(R_QuitMsg.class);
	
	public void process(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		QuitMsg quitmsg = (QuitMsg) message;
		log.info(quitmsg.getUser().getNickname()
				+ " has quit"
				+ ((quitmsg.getMessage() == null) ? "" : (": " + quitmsg
						.getMessage())));
	}

	public boolean reactsTo(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		return true;
	}
	
	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(QuitMsg.class);
	}

}
