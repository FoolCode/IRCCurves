package curves.trigger.system;

import java.util.Hashtable;

import org.apache.log4j.Logger;

import curves.main.*;
import curves.message.*;
import curves.trigger.IReadHandler;

public class R_NickTaken implements IReadHandler {

	Logger log = Logger.getLogger(R_NickTaken.class);
	
	public void process(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		// change name and use ghost (nickserv) command
		// then login as usual
		storage.put("nick taken", "true");
		log.warn("Nickname is taken, trying to recover using GHOST command.");
		Profile primary = bot.getProfile();
		Profile secondary = new Profile(primary.getNickname()
				+ ((int) Math.floor((Math.random() * 1000))), primary
				.getHostname(), primary.getServername(), primary.getRealname(),
				null);
		bot.send(new NickMsg(secondary));
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			log.error("Sleep failed.", e);
		}
		bot.send(new PrivMsg("NickServ", "GHOST " + primary.getNickname() + " "
				+ primary.getNickserv()));
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			log.error("Sleep failed.", e);
		}
		bot.send(new NickMsg(primary));
		if (bot.getProfile().getNickname() != null)
			bot.send(new PrivMsg("NickServ", "IDENTIFY "
					+ bot.getProfile().getNickserv()));
		for (Channel channel : bot.getProfile().getChannels()) {
			bot.send(new JoinMsg(channel));
		}
		storage.remove("nick taken");
	}

	public boolean reactsTo(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		return (message instanceof Generic && message.getOriginal().contains(
				"Nickname is already in use"));
	}
	
	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(Generic.class);
	}
}
