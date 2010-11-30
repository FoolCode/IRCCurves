package curves.trigger.mana;

import java.util.Hashtable;

import curves.main.Bot;
import curves.main.Profile;
import curves.message.IMessage;
import curves.message.PrivMsg;
import curves.trigger.IReadHandler;

public class R_QueryStats implements IReadHandler {

	public void process(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		Profile user = message.getUser();
		Creature creature =  new Creature(user, bot);
		if (creature.exists()){
		bot.send(new PrivMsg(user.getNickname(), "You got "
				+ LibMana.getMana(message.getUser(), bot)
				+ "MP and your creature is a " + creature + ". http://foolrulez.org/tanline/"));
		} else {
			bot.send(new PrivMsg(user.getNickname(), "You got "
					+ LibMana.getMana(message.getUser(), bot)
					+ "MP and you don't have a creature yet. http://foolrulez.org/tanline/"));
		}
	}

	public boolean reactsTo(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		return ((PrivMsg) message).getMessage().startsWith("!stats");
	}
	
	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(PrivMsg.class);
	}

}
