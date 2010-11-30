package curves.trigger.query;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import curves.main.Bot;
import curves.message.IMessage;
import curves.message.PrivMsg;
import curves.trigger.IReadHandler;
import curves.trigger.lib.LibTinyUrl;

public class R_Wiki implements IReadHandler {
	
	Logger log = Logger.getLogger(R_Wiki.class);

	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(PrivMsg.class);
	}

	public void process(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		try {
			String query = ((PrivMsg) message).trailing();
			URL url = new URL(
					"http://en.wikipedia.org/w/api.php?action=opensearch&limit=3&format=json&search="
							+ URLEncoder.encode(query, "UTF-8"));
			URLConnection connection = url.openConnection();
			String line;
			StringBuilder builder = new StringBuilder();
			BufferedReader reader;
			reader = new BufferedReader(new InputStreamReader(connection
					.getInputStream(), "UTF-8"));
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
			Pattern p = Pattern.compile("^\\[\".*?\",\\[(.*?)\\]\\]$");
			Matcher m = p.matcher(builder.toString());
			if (m.find()) {
				String text = "";
				String[] found = m.group(1).split("\",\"|\",|\"");
				for (int i = 1; i < found.length; i++) {
					text = text
							+ found[i]
							+ ": "
							+ LibTinyUrl
									.shorten("http://en.wikipedia.org/wiki/"
											+ found[i].replace(" ", "_"))
							+ "  ";
				}
				((PrivMsg) message).reply(bot, text);
			} else {
				bot.send(new PrivMsg(message.getUser().getNickname(),
						"Sorry, I found nothing about '" + query + "'."));
			}
		} catch (Exception e) {
			log.error("Unable to fetch data from Wikipedia.", e);
		}
	}

	public boolean reactsTo(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		return ((PrivMsg) message).getMessage().startsWith("!wiki ");
	}

}
