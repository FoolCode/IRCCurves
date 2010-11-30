package curves.trigger.query;

import java.util.Hashtable;

import org.apache.commons.lang.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import curves.main.Bot;
import curves.message.IMessage;
import curves.message.PrivMsg;
import curves.trigger.IReadHandler;
import curves.trigger.lib.LibGoogle;
import curves.trigger.lib.LibTinyUrl;

public class R_GoogleImages implements IReadHandler {

	public void process(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		String substring = ((PrivMsg) message).trailing();
		String answer;
		try {
			JSONObject json = LibGoogle.googleQuery("images", substring);
			json = (JSONObject) json.get("responseData");
			JSONArray jsonarray = (JSONArray) json.get("results");
			json = (JSONObject) jsonarray.get(0);
			answer = StringEscapeUtils.unescapeHtml(json
					.getString("titleNoFormatting"))
					+ " - ["
					+ json.getString("visibleUrl")
					+ "] "
					+ LibTinyUrl.shorten(json.getString("unescapedUrl"));
			((PrivMsg) message).reply(bot, answer);
		} catch (Exception e) {
			answer = "Sorry, I found no image for \"" + substring + "\".";
			bot.send(new PrivMsg(message.getUser().getNickname(), answer));
		}
	}

	public boolean reactsTo(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		return ((PrivMsg) message).getMessage().startsWith("!image ");
	}

	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(PrivMsg.class);
	}
}
