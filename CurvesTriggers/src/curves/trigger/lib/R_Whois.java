package curves.trigger.lib;

import java.util.Hashtable;

import org.apache.log4j.Logger;

import curves.main.Bot;
import curves.main.Profile;
import curves.message.IMessage;
import curves.message.IWhoisReplyMsg;
import curves.message.WhoisChannelsMsg;
import curves.message.WhoisEndMsg;
import curves.message.WhoisIdentifiedMsg;
import curves.message.WhoisMsg;
import curves.message.WhoisNameMsg;
import curves.trigger.IReadHandler;

public class R_Whois implements IReadHandler {

	static Logger log = Logger.getLogger(R_Whois.class);
	static Hashtable<String, Profile> pool = new Hashtable<String, Profile>();

	/**
	 * @return a best effort info for the queried profile
	 */
	public static Profile getInfo(Profile profile, Bot bot) {

		log.info("Getting WHOIS information about " + profile.getNickname());
		synchronized (pool) {
			// if query already started once and has not yet finished
			// just point to the same result profile
			if (pool.contains(profile.getNickname())) {
				profile = pool.get(profile.getNickname());
			} else {
				pool.put(profile.getNickname(), profile);
			}
		}

		bot.send(new WhoisMsg(profile));
		long timer = 0;
		while (timer++ < 20 && pool.containsKey(profile.getNickname())) {
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				log.error("Sleep failed.", e);
			}
		}
		return profile;
	}

	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(WhoisChannelsMsg.class)
				|| messageType.equals(WhoisIdentifiedMsg.class)
				|| messageType.equals(WhoisNameMsg.class)
				|| messageType.equals(WhoisEndMsg.class);
	}

	public void process(IMessage message, Bot bot,
			Hashtable<String, Object> storage) {

		Profile profile = pool.get(((IWhoisReplyMsg) message).getProfile()
				.getNickname());
		if (message instanceof WhoisEndMsg) {
			synchronized (pool) {
				pool.remove(profile.getNickname());
			}
			return;
		} else {
			profile.mergeWith(((IWhoisReplyMsg) message).getProfile());
		}

	}

	public boolean reactsTo(IMessage message, Bot bot,
			Hashtable<String, Object> storage) {
		return (pool != null)
				&& pool.containsKey(((IWhoisReplyMsg) message).getProfile()
						.getNickname());
	}
}
