package curves.trigger.query;

import java.util.Hashtable;

import curves.main.Bot;
import curves.main.Channel;
import curves.main.Profile;
import curves.message.IMessage;
import curves.message.PrivMsg;
import curves.trigger.IReadHandler;
import curves.trigger.lib.R_Whois;

public class R_Bounce implements IReadHandler {

	public void process(IMessage message, Bot bot,
			Hashtable<String, Object> storage) {
		Profile profile = message.getUser();
		R_Whois.getInfo(profile, bot);
		((PrivMsg) message).reply(bot, profile.getRealname());
		((PrivMsg) message)
				.reply(bot, ""
						+ profile.getPriv(new Channel(((PrivMsg) message)
								.getTarget())));
	}

	public boolean reactsTo(IMessage message, Bot bot,
			Hashtable<String, Object> storage) {
		return ((PrivMsg) message).getMessage().startsWith("!bounce ");
	}

	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(PrivMsg.class);
	}
}
