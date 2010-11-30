package curves.trigger.foolrulez;

import java.util.Hashtable;

import org.apache.log4j.Logger;

import curves.main.Bot;
import curves.main.Channel;
import curves.main.Profile;
import curves.message.IMessage;
import curves.message.KickMsg;
import curves.message.ModeMsg;
import curves.message.PrivMsg;
import curves.trigger.IReadHandler;
import curves.trigger.lib.MainChannel;
import curves.trigger.lib.R_Whois;

public class R_Itterasshai implements IReadHandler {

	Logger log = Logger.getLogger(R_Itterasshai.class);
	
	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(PrivMsg.class);
	}

	public void process(IMessage message, Bot bot,
			Hashtable<String, Object> storage) {
		PrivMsg msg = ((PrivMsg) message);
		Channel channel = new Channel(msg.getTarget());
		Profile kicker = message.getUser();
		String kickedName = msg.trailing().split(" ")[0];
		if (kickedName.toLowerCase().equals("saber")) return;
		if (kickedName.toLowerCase().equals("fool")) return;
		if (kickedName == null || kickedName.equals(""))
			return;
		Profile kicked = new Profile(kickedName);
		kicker = R_Whois.getInfo(kicker, bot);
		if (kicker.getPriv(channel) < Profile.OP) {
			return;
		}
		kicked = R_Whois.getInfo(kicked, bot);
		int mask = Profile.ADMIN | Profile.OP;
		if ((kicker.getPriv(channel) & mask) <= (kicked.getPriv(channel) & mask)) {
			return;
		}
		bot.send(new ModeMsg(channel, "+b", kicked.getNickname()+"!*@*"));
		bot.send(new KickMsg(channel, "Itterasshai!", kicked));
		try {
			Thread.sleep(6000);
		} catch (InterruptedException e) {
			log.error("Sleep failed.", e);
		}
		bot.send(new ModeMsg(channel, "-b", kicked.getNickname()+"!*@*"));
	}

	public boolean reactsTo(IMessage message, Bot bot,
			Hashtable<String, Object> storage) {
		return (((PrivMsg) message).getMessage().startsWith("!itterasshai ") || ((PrivMsg) message)
				.getMessage().startsWith("!itt "))
				&& MainChannel.in(message, storage);
	}

}
