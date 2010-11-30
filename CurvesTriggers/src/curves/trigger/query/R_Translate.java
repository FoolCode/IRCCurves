package curves.trigger.query;

import java.util.Hashtable;

import org.apache.log4j.Logger;

import com.google.api.detect.Detect;
import com.google.api.translate.Language;
import com.google.api.translate.Translate;

import curves.main.Bot;
import curves.message.IMessage;
import curves.message.PrivMsg;
import curves.trigger.IReadHandler;

public class R_Translate implements IReadHandler {

	Logger log = Logger.getLogger(R_Translate.class);
	
	public void process(IMessage message, Bot bot,
			Hashtable<String, Object> storage) {
		try {
			String text = ((PrivMsg) message).trailing();
			Detect.setHttpReferrer("www.google.de");
			Translate.setHttpReferrer("www.google.de");
			Language from = Detect.execute(text).getLanguage();
			Language to = Language.fromString(((PrivMsg) message).getMessage()
					.substring(2, 4).toLowerCase());
			if (to == null)
				to = Language.ENGLISH;
			String translatedText = Translate.execute(text, from, to);
			((PrivMsg) message).reply(bot, from.name().toLowerCase() + " to "
					+ to.name().toLowerCase() + ": " + translatedText);
		} catch (Exception e) {
			log.error("Google Translation API failed.", e);
		}
	}

	public boolean reactsTo(IMessage message, Bot bot,
			Hashtable<String, Object> storage) {
		return ((PrivMsg) message).getMessage().startsWith("!2")
				&& ((PrivMsg) message).getMessage().split(" ")[0].length() == 4;
	}

	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(PrivMsg.class);
	}

}
