package curves.trigger.foolrulez;

import java.util.Hashtable;

import curves.main.Bot;
import curves.main.Channel;
import curves.message.IMessage;
import curves.message.KickMsg;
import curves.message.PrivMsg;
import curves.trigger.IReadHandler;
import curves.trigger.lib.MainChannel;

public class R_Roulette implements IReadHandler {

	public void process(IMessage message, Bot bot,
			Hashtable<String, Object> storage) {
		PrivMsg msg = (PrivMsg) message;
		if (Math.random()*3 > 2){
			msg.reply(bot, "Bang, gotcha!");
			bot.send(new KickMsg(new Channel(msg.getTarget()), "bullet in your head.", msg.getUser()));
		} else {
			msg.reply(bot, "Click!");
		}
	}

	public boolean reactsTo(IMessage message, Bot bot,
			Hashtable<String, Object> storage) {
		return ((PrivMsg) message).getMessage().startsWith("!roulette")
				&& MainChannel.in(message, storage);
	}

	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(PrivMsg.class);
	}

}
