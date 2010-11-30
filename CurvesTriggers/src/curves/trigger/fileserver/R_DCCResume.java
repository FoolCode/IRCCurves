package curves.trigger.fileserver;

import java.util.Hashtable;

import curves.main.Bot;
import curves.message.IMessage;
import curves.message.PrivMsg;
import curves.trigger.IReadHandler;

public class R_DCCResume implements IReadHandler {

	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(PrivMsg.class);
	}

	public void process(IMessage message, Bot bot,
			Hashtable<String, Object> storage) {
		char CTCPchar = 1;
		String CTCP = "" + CTCPchar + "";
		String[] split = ((PrivMsg) message).getMessage().replace(CTCP, "")
				.split(" ");
		String user = message.getUser().toString();
		String file = split[2];
		String port = split[3];
		String position = split[4];
		String id = user + " " + file + " " + port;
		storage.put(id, position);
		PrivMsg reply = new PrivMsg(message.getUser().getNickname(), CTCP
				+ "DCC ACCEPT " + file + " " + port + " " + position + CTCP);
		bot.send(reply);
	}

	public boolean reactsTo(IMessage message, Bot bot,
			Hashtable<String, Object> storage) {
		PrivMsg msg = ((PrivMsg) message);
		char CTCPchar = 1;
		String CTCP = "" + CTCPchar + "";
		return msg.getTarget().equals(bot.getProfile().getNickname())
				&& msg.getMessage().startsWith(CTCP + "DCC RESUME");
	}

}
