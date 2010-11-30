package curves.trigger.system;

import java.util.Hashtable;

import curves.main.Bot;
import curves.main.Channel;
import curves.message.IMessage;
import curves.message.InviteMsg;
import curves.message.JoinMsg;
import curves.trigger.IReadHandler;

/**
 * react on invitations of an admin.
 */
public class R_AcceptInvite implements IReadHandler {

	public void process(IMessage message, Bot bot,
			Hashtable<String, Object> storage) {
		Channel channel = ((InviteMsg) message).getChannel();
		bot.send(new JoinMsg(channel));
		bot.getProfile().addChannel(channel, 0);
	}

	public boolean reactsTo(IMessage message, Bot bot,
			Hashtable<String, Object> storage) {
		return message instanceof InviteMsg
				&& ((String) storage.get("accept invite")).equals("true")
				&& message.isFromAdminOf(bot);
	}

	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(InviteMsg.class);
	}
}
