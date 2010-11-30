package curves.trigger.query;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import curves.main.Bot;
import curves.message.IMessage;
import curves.message.PrivMsg;
import curves.trigger.IReadHandler;
import curves.trigger.lib.LibWeb;

public class R_Wikipath implements IReadHandler {

	Logger log = Logger.getLogger(R_Wikipath.class);

	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(PrivMsg.class);
	}

	public void process(IMessage message, Bot bot,
			Hashtable<String, Object> storage) {
		PrivMsg msg = (PrivMsg) message;
		String[] split = msg.trailing().split(", ");
		String from = split[0];
		String to = split[1];
		if (from.toLowerCase().equals(to.toLowerCase())) {
			msg.reply(bot, "You are trying to trick me, aren't you,");
			return;
		}
		log
				.info("Searching for wiki path from '" + from + "' to '" + to
						+ "'.");
		String url = "";
		try {
			url = "http://www.netsoc.tcd.ie/~mu/cgi-bin/shortpath.cgi?from="
					+ URLEncoder.encode(from, "utf8") + "&to="
					+ URLEncoder.encode(to, "utf8");
		} catch (UnsupportedEncodingException e) {
			log.error("Encoding URL failed.", e);
		}
		String results = LibWeb.getPage(url);
		Pattern pattern = Pattern
				.compile("<a href=\"http:\\/\\/en\\.wikipedia\\.org\\/wiki\\/.*?\">(.*?)<\\/a>");
		Matcher matcher = pattern.matcher(results);
		ArrayList<String> terms = new ArrayList<String>();
		while (matcher.find())
			terms.add(matcher.group(1));
		String reply = "";
		if (terms.size() < 1) {
			reply = "I had trouble with the search terms.";
		} else {
			for (int i = 0; i < terms.size() - 1; i++) {
				reply += terms.get(i) + " -> ";
			}
			reply += terms.get(terms.size() - 1);
		}
		msg.reply(bot, reply);
	}

	public boolean reactsTo(IMessage message, Bot bot,
			Hashtable<String, Object> storage) {
		return ((PrivMsg) message).getMessage().startsWith("!wikipath ");
	}

}
