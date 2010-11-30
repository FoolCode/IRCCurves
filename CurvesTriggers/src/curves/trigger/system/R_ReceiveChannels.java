package curves.trigger.system;

import java.util.Hashtable;

import org.apache.log4j.Logger;

import curves.main.Bot;
import curves.main.Channel;
import curves.main.Profile;
import curves.message.IMessage;
import curves.message.WhoisChannelsMsg;
import curves.trigger.IReadHandler;

/**
 * Keeps the channels list of the bot up-to-date, works together with
 * P_QueryChannels
 */

public class R_ReceiveChannels implements IReadHandler {

	Logger log = Logger.getLogger(R_ReceiveChannels.class);

	public void process(IMessage message, Bot bot,
			Hashtable<String, Object> storage) {
		Profile profile = bot.getProfile();
		synchronized (profile) {
			profile.getChannelTable().clear();
			profile.mergeWith(((WhoisChannelsMsg) message).getProfile());
			for (Channel channel : bot.getProfile().getChannels()) {
				log.debug("Currently in channel: " + channel.getChannel() + "("
						+ profile.getPriv(channel) + ")");
			}
		}
	}

	public boolean reactsTo(IMessage message, Bot bot,
			Hashtable<String, Object> storage) {
		return ((WhoisChannelsMsg) message).getProfile().equals(
				bot.getProfile());
	}

	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(WhoisChannelsMsg.class);
	}

}
