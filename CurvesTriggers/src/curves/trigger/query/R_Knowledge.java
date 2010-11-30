package curves.trigger.query;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import curves.main.Bot;
import curves.message.IMessage;
import curves.message.PrivMsg;
import curves.trigger.IReadHandler;

public class R_Knowledge implements IReadHandler {

	Logger log = Logger.getLogger(R_Knowledge.class);

	public void process(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		String answer = null;
		String substring = ((PrivMsg) message).getMessage();
		boolean alternative = false;
		if (substring.startsWith("!?")) {
			substring = substring.substring(3);
			alternative = true;
		}
		try {
			String question = URLEncoder.encode(substring, "UTF-8");
			String user = (String) storage.get("knowledge API user");
			String pass = (String) storage.get("knowledge API pass");
			if (user == null || pass == null || user.equals("") || pass.equals("")){
				log.warn("please configure TrueKnowledge API account settings!");
				return;
			}
			
			answer = query(question, user, pass);
			
		} catch (IOException e) {
			log.debug("Received no answer from TrueKnowledge", e);
			answer = null;
		}
		if (answer == null || answer.equals("")) {
			if (alternative) {
				new R_GoogleWeb().process(message, bot, storage);
			}
		} else {
			((PrivMsg) message).reply(bot, answer);
		}
	}

	private String query(String question, String user,
			String pass) throws MalformedURLException, IOException,
			UnsupportedEncodingException {
		String answer = "";
		URL serverAddress = new URL(
				"http://api.trueknowledge.com/direct_answer?question="
						+ question + "&api_account_id=" + user
						+ "&api_password=" + pass
						+ "&timeout_ms=20000&structured_response=0");
		HttpURLConnection connection = (HttpURLConnection) serverAddress
				.openConnection();
		InputStreamReader isr;
		isr = new InputStreamReader(connection.getInputStream(), "UTF-8");
		BufferedReader reader = new BufferedReader(isr);
		Pattern p = Pattern
				.compile("<tk:text_result>(.*?)</tk:text_result>");
		String line;
		while ((line = reader.readLine()) != null) {
			Matcher m = p.matcher(line);
			if (m.find()) {
				answer = m.group(1);
				break;
			}
		}
		return answer;
	}

	public boolean reactsTo(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		String msg = ((PrivMsg) message).getMessage();
		return (msg.startsWith("!?")) || (msg.endsWith("?") && msg.length() > 10);
	}
	
	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(PrivMsg.class);
	}

}
