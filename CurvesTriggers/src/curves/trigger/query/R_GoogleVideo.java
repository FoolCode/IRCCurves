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

public class R_GoogleVideo implements IReadHandler {

	public void process(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		String substring = ((PrivMsg) message).trailing();
		String answer;
		try {
			JSONObject json = LibGoogle.googleQuery("video", substring);
			json = (JSONObject) json.get("responseData");
			JSONArray jsonarray = (JSONArray) json.get("results");
			json = (JSONObject) jsonarray.get(0);
			String url = json.getString("playUrl");
			url = url.replace("http://www.youtube.com/v/",
					"http://www.youtube.com/watch?v=");
			answer = StringEscapeUtils.unescapeHtml(json
					.getString("titleNoFormatting"))
					+ " ["
					+ json.getString("duration")
					+ "s] - ["
					+ json.getString("publisher")
					+ "] "
					+ LibTinyUrl.shorten(url);
			((PrivMsg) message).reply(bot, answer);
		} catch (Exception e) {
			answer = "Sorry, I found no video for \"" + substring + "\".";
			bot.send(new PrivMsg(message.getUser().getNickname(), answer));
		}
	}

	public boolean reactsTo(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		return ((PrivMsg) message).getMessage().startsWith("!video ");
	}

	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(PrivMsg.class);
	}

}
