package curves.trigger.mana;

import java.util.Hashtable;

import curves.main.Bot;
import curves.main.Profile;
import curves.message.IMessage;
import curves.message.KickMsg;
import curves.message.PrivMsg;
import curves.trigger.IReadHandler;
import curves.trigger.lib.MainChannel;

public class R_Duel implements IReadHandler {

	double oddsCoeff = 8; // the smaller, the steeper the odds sigmoid curve
	double defCoeff = 2; // the bigger, the less bonus effect def has.
	Profile userA = null;
	Profile userB = null;
	boolean killedA = false;
	boolean killedB = false;
	Creature creatureA = null;
	Creature creatureB = null;
	int creatureACost = 0;
	int creatureBCost = 0;
	
	private boolean payMana(IMessage message, Bot bot,
			Hashtable<String, Object> storage){
		if (!LibMana.payMana(2, message.getUser(), bot)) {
			int mana = LibMana.getMana(message.getUser(), bot);
			if (mana < 0){
				LibMana.newPlayer(message.getUser(), bot);
				mana = 0;
			}
			bot.send(new PrivMsg(message.getUser().getNickname(),
					"You dont have enough Mana (" + mana + ")."));
			return false;
		}
		return true;
	}
	
	private boolean init(IMessage message, Bot bot,
			Hashtable<String, Object> storage){
		userA = (Profile) storage.get("duel user");
		userB = message.getUser();
		if (userA.equals(userB)) {
			((PrivMsg) message).reply(bot, "Don't fight yourself kthx.");
			return false;
		}
		creatureA = new Creature(userA, bot);
		creatureB = new Creature(userB, bot);
		creatureACost = creatureA.getCost();
		creatureBCost = creatureB.getCost();
		killedA = false;
		killedB = false;
		return true;
	}

	public synchronized void process(IMessage message, Bot bot,
			Hashtable<String, Object> storage) {
		
		if (!payMana(message, bot, storage)) return;
		
		if (storage.containsKey("duel user")) {
			
			if (!init(message, bot, storage)) return;
			// determine who gets killed;
			fight();

			// determine who gets killed
			String text = casualty();

			// TERRORIST attribute
			text = terrorist(text);

			// SACRIFICE attribute
			text = sacrifice(text);

			// MESSAGE OUT
			((PrivMsg) message).reply(bot, text);

			levelup(message, bot);

			// KICK
			kick(bot, storage);

			storage.remove("duel user");
		} else {
			((PrivMsg) message).reply(bot, message.getUser().getNickname()
					+ " has challenged to a duel!");
			storage.put("duel user", message.getUser());

		}
	}

	private void kick(Bot bot, Hashtable<String, Object> storage) {
		if (killedA) {
			bot.send(new KickMsg(MainChannel.get(storage), "You died.",
					userA));
		}
		if (killedB) {
			bot.send(new KickMsg(MainChannel.get(storage), "You died.",
					userB));
		}
	}

	private void levelup(IMessage message, Bot bot) {
		levelupA(message, bot);
		levelupB(message, bot);
	}

	private void levelupB(IMessage message, Bot bot) {
		if (!creatureB.died()) {
			// LEVEL UP
			if (!killedB && creatureB.exists()
					&& creatureB.levelup(creatureACost)) {
				((PrivMsg) message).reply(bot, userB.getNickname()
						+ "'s creature evolved into a " + creatureB + ".");
			}
			// FIST LEVEL UP
			if (!killedB && !creatureB.exists()
					&& creatureB.levelup(creatureACost)) {
				((PrivMsg) message).reply(bot, userB.getNickname()
						+ " has learned " + creatureB + ".");
			}
		}
	}

	private void levelupA(IMessage message, Bot bot) {
		if (!creatureA.died()) {
			// LEVEL UP
			if (!killedA && creatureA.exists()
					&& creatureA.levelup(creatureBCost)) {
				((PrivMsg) message).reply(bot, userA.getNickname()
						+ "'s creature evolved into a " + creatureA + ".");
			}
			// FIST LEVEL UP
			if (!killedA && !creatureA.exists()
					&& creatureA.levelup(creatureBCost)) {
				((PrivMsg) message).reply(bot, userA.getNickname()
						+ " has learned " + creatureA + ".");
			}
		}
	}

	private String sacrifice(String text) {
		if (killedA && creatureA.exists()
				&& (creatureA.getAttribute() & Creature.SACRIFICE) != 0) {

			text = text + " " + userA.getNickname() + "'s " + creatureA
					+ " sacrificed itself to save its owner.";
			creatureA.dies();
			killedA = false;
		}
		if (killedB && creatureB.exists()
				&& (creatureB.getAttribute() & Creature.SACRIFICE) != 0) {
			text = text + " " + userB.getNickname() + "'s " + creatureB
					+ " sacrificed itself to save its owner.";
			creatureB.dies();
			killedB = false;
		}
		return text;
	}

	private String terrorist(String text) {
		if (killedA && !killedB
				&& (creatureA.getAttribute() & Creature.TERRORIST) != 0) {
			text = text + " " + userA.getNickname() + "'s " + creatureA
					+ " sacrificed itself to kill " + userB.getNickname()
					+ ".";
			killedB = true;
			creatureA.dies();
		}
		if (killedB && !killedA
				&& (creatureB.getAttribute() & Creature.TERRORIST) != 0) {
			text = text + " " + userB.getNickname() + "'s " + creatureB
					+ " sacrificed itself to kill " + userA.getNickname()
					+ ".";
			killedA = true;
			creatureB.dies();
		}
		return text;
	}

	private String casualty() {
		String text = "";
		if (killedA && killedB) {
			text = "Both " + userA.getNickname() + " with his/her "
					+ creatureA + " and " + userB.getNickname()
					+ " with his/her " + creatureB
					+ " suffered lethal wounds.";
		} else if (killedA) {
			text = userB.getNickname() + " with his/her " + creatureB
					+ " defeated " + userA.getNickname() + " and his/her "
					+ creatureA + ".";

		} else if (killedB) {
			text = userA.getNickname() + " with his/her " + creatureA
					+ " defeated " + userB.getNickname() + " and his/her "
					+ creatureB + ".";
		} else {
			text = "Neither " + userA.getNickname() + " (" + creatureA
					+ ") nor " + userB.getNickname() + " (" + creatureB
					+ ") could win.";
		}
		return text;
	}

	private void fight() {
		for (int i = 0; i < 2; i++) {
			killedA = killedB = false;
			if ((Math.random() > 1.0 / (1.0 + Math
					.exp((creatureA.getAtk() - creatureB.getDef())
							/ oddsCoeff)))
					&&

					(Math.random() < (0.5 + 1.0 / (1.0 + Math.exp(creatureB
							.getDef()
							/ oddsCoeff - defCoeff))))) {
				// A has killed B
				killedB = true;
			}
			if ((Math.random() > 1.0 / (1 + Math
					.exp((creatureB.getAtk() - creatureA.getDef())
							/ oddsCoeff)))
					&& ((Math.random() * creatureA.getDef() / 5 < 1))) {
				// A has killed B
				killedA = true;
			}
			if (killedA ^ killedB) {
				break;
			}
		}
	}

	public boolean reactsTo(IMessage message, Bot bot,
			Hashtable<String, Object> storage) {
		return MainChannel.in(message, storage)
				&& ((PrivMsg) message).getMessage().startsWith("!duel");
	}

	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(PrivMsg.class);
	}

}
