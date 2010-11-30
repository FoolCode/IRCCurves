package curves.trigger.mana;

import java.util.Hashtable;

import curves.main.Bot;
import curves.main.Profile;
import curves.message.IMessage;
import curves.message.PrivMsg;
import curves.trigger.IReadHandler;
import curves.trigger.lib.MainChannel;

public class R_SummonCreature implements IReadHandler {

	public void process(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		Profile user = message.getUser();
		Creature creature = new Creature(user, bot);
		if (creature.exists()) {
			((PrivMsg) message).reply(bot, "You already have a "
					+ creature.getName() + ", "
					+ message.getUser().getNickname() + ".");
		} else {
			creature = new Creature(user, LibMana.getMana(user, bot), bot);
			if (!creature.exists()) {
				bot.send(new PrivMsg(user.getNickname(),
						"You don't have enough mana, "
								+ message.getUser().getNickname() + "."));
			} else {
				((PrivMsg) message).reply(bot, "You summoned a "
						+ creature.getName() + ", "
						+ message.getUser().getNickname() + ".");
			}
		}
	}

	public boolean reactsTo(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		return MainChannel.in(message, storage)
				&& ((PrivMsg) message).getMessage().startsWith("!summon");
	}

	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(PrivMsg.class);
	}

}
