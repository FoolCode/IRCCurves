package curves.trigger.query;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Hashtable;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import curves.main.Bot;
import curves.message.IMessage;
import curves.message.PrivMsg;
import curves.trigger.IReadHandler;
import curves.trigger.lib.LibGoogle;

public class R_GoogleFight implements IReadHandler {

	Logger log = Logger.getLogger(R_GoogleFight.class);

	public void process(IMessage message, Bot bot,
			Hashtable<String, Object> storage) {
		PrivMsg msg = (PrivMsg) message;
		String substring = ((PrivMsg) message).trailing();
		long a[] = { 0, 0 };
		String[] f = substring.split(",");
		if (f.length < 2)
			return;
		JSONObject json = null;
		for (int i = 0; i < 2; i++)
			try {
				String term = f[i].trim();
				if (term.equals("") || term == null)
					return;
				json = LibGoogle.googleQuery("web", term);
				json = (JSONObject) json.get("responseData");
				json = (JSONObject) json.get("cursor");
				a[i] = json.getLong("estimatedResultCount");
			} catch (Exception e) {
				String answer = "Sorry, couldn't get any data from Google.";
				bot.send(new PrivMsg(message.getUser().getNickname(), answer));
				return;
			}
		NumberFormat format = new DecimalFormat("0.##E0");
		msg.reply(bot, f[0] + "(" + format.format(a[0]) + ")" + " vs " + f[1]
				+ "(" + format.format(a[1]) + "), "
				+ ((a[0] > a[1]) ? f[0] : f[1]) + " wins!");
	}

	public boolean reactsTo(IMessage message, Bot bot,
			Hashtable<String, Object> storage) {
		return ((PrivMsg) message).getMessage().startsWith("!fight ");
	}

	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(PrivMsg.class);
	}

}
