package curves.trigger.query;

import java.util.Hashtable;
import java.util.Random;

import curves.main.Bot;
import curves.message.IMessage;
import curves.message.PrivMsg;
import curves.trigger.IReadHandler;

public class R_EightBall implements IReadHandler {

	String[] answers = {
			"Signs point to yes, #.",
			"Yes, #.",
			"Reply hazy, #. Try again.",
			"Without a doubt, #.",
			"#, my sources say no.",
			"As I see it, #, yes.",
			"I think so, #.",
			"#, you may rely on it.",
			"Concentrate and ask again, #.",
			"Outlook not so good, #.",
			"It is decidedly so, #.",
			"Better not tell you now, #.",
			"Very doubtful, #.",
			"Yes, #, definitely.",
			"It is certain, #.",
			"Cannot predict now, #.",
			"Most likely, #.",
			"Ask again later, #.",
			"#, no.",
			"Hell no, #.",
			"Outlook good, #.",
			"Don't count on it, #.",
			"In your dreams, #.",
			"Haha, #, you fool.",
			"What made you think that, #?",
			"Seriously, #? LOL.",
			"#, you better ask Woxxy...",
			"I sure hope so, #.",
			"42, #.",
			"Why the hell would I know that, #?",
			"Hm? I didn't quite understand you, #.",
			"Yes, #! Don't believe in yourself, believe in me who believes in you!",
			"Geez, #, I'm not your mom!",
			"#, you've got to ask yourself one question: 'Do I feel lucky?' Well, do ya punk?",
			"No, sorry, #, that's Top Secret.", "#, Just Fucking Google It.",
			"*blush* I don't think I should answer that, #.",
			"Go flip a coin, #." };

	public void process(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		Random rand = new Random((message.getOriginal() + Math.round(System
				.currentTimeMillis() / 300000)).hashCode());
		String answer = answers[rand.nextInt(answers.length)].replace("#",
				message.getUser().getNickname());
		((PrivMsg) message).reply(bot, answer);
	}

	public boolean reactsTo(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		return ((PrivMsg) message).getMessage().startsWith("!8ball ")
				&& ((PrivMsg) message).getMessage().endsWith("?");
	}
	
	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(PrivMsg.class);
	}

}
