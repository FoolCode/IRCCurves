package curves.trigger.system;

import java.util.Hashtable;

import org.apache.log4j.Logger;

import curves.main.Bot;
import curves.message.IMessage;
import curves.message.JoinMsg;
import curves.message.KickMsg;
import curves.message.PrivMsg;
import curves.trigger.IReadHandler;

/**
 * automatically rejoin a channel unless kicked by an admin
 */
public class R_AutoRejoin implements IReadHandler {

	Logger log = Logger.getLogger(R_AutoRejoin.class);

	public void process(IMessage message, Bot bot,
			Hashtable<String, Object> storage) {
		KickMsg kick = (KickMsg) message;
		if (message.isFromAdminOf(bot)) {
			bot.getProfile().removeChannel(kick.getChannel());
		} else {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				log.error("Sleep failed.", e);
			}
			bot.send(new PrivMsg("ChanServ", "UNBAN " + kick.getChannel()));
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				log.error("Sleep failed.", e);
			}
			bot.send(new JoinMsg(kick.getChannel()));
			bot.send(new KickMsg(kick.getChannel(), "revenge", kick.getUser()));
		}
	}

	public boolean reactsTo(IMessage message, Bot bot,
			Hashtable<String, Object> storage) {
		return ((KickMsg) message).getTarget().getNickname().equals(
				bot.getProfile().getNickname())
				&& ((String) storage.get("auto rejoin")).equals("true");
	}

	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(KickMsg.class);
	}

}
