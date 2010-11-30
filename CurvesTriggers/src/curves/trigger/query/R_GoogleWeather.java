package curves.trigger.query;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import curves.main.Bot;
import curves.message.IMessage;
import curves.message.PrivMsg;
import curves.trigger.IReadHandler;

public class R_GoogleWeather implements IReadHandler {

	public static int fahrenheitToCelsius(int tFahrenheit) {
		return (int) ((5.0f / 9.0f) * (tFahrenheit - 32));
	}

	public String getLocation(String xmlstring) {
		Pattern pattern = Pattern.compile("city data=\"(.*?)\"",
				Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
		Matcher match = pattern.matcher(xmlstring);
		while (match.find()) {
			return match.group(1);
		}
		return null;
	}

	public String getCurrent(String xmlstring) {
		Pattern pattern = Pattern
				.compile(
						"<current_conditions>.*?condition data=\"(.*?)\".*?temp_c data=\"(.*?)\".*?</current_conditions>",
						Pattern.CASE_INSENSITIVE | Pattern.DOTALL
								| Pattern.MULTILINE);
		Matcher match = pattern.matcher(xmlstring);
		if (match.find()) {
			return match.group(1) + " at " + match.group(2) + "»C";
		}
		return "";
	}

	public String getFuture(String xmlstring) {
		Pattern pattern = Pattern
				.compile(
						"<forecast_conditions>.*?day_of_week data=\"(.*?)\".*?low data=\"(.*?)\".*?.*?high data=\"(.*?)\".*?condition data=\"(.*?)\".*?</forecast_conditions>",
						Pattern.CASE_INSENSITIVE | Pattern.DOTALL
								| Pattern.MULTILINE);
		Matcher match = pattern.matcher(xmlstring);
		String out = "";
		int i = 0;
		while (match.find()) {
			int low = fahrenheitToCelsius(Integer.parseInt(match.group(2)));
			int high = fahrenheitToCelsius(Integer.parseInt(match.group(3)));
			out = out + match.group(1) + ": " + match.group(4) + " at " + low
					+ "/" + high + "»C";
			if (i++ > 1)
				break;
			out = out + ", ";

		}
		return out;
	}

	public void process(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		String substring = ((PrivMsg) message).trailing();
		try {
			URL url = new URL("http://www.google.com/ig/api?weather="
					+ URLEncoder.encode(substring, "UTF-8"));
			URLConnection con = url.openConnection();
			// Get the response
			BufferedReader rd = new BufferedReader(new InputStreamReader(con
					.getInputStream()));
			String line;
			String xmlstring = "";
			while ((line = rd.readLine()) != null) {
				xmlstring = xmlstring + line;
			}
			rd.close();

			String location = getLocation(xmlstring);
			String answer;
			if (location != null)
				answer = "Weather in '" + location + "' currently: "
						+ getCurrent(xmlstring) + ". " + getFuture(xmlstring);
			else
				answer = "Google Weather is having problems with the term \""
						+ substring + "\"";
			((PrivMsg) message).reply(bot, answer);
		} catch (Exception e) {
		}
	}

	public boolean reactsTo(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		return ((PrivMsg) message).getMessage().startsWith("!weather ");
	}

	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(PrivMsg.class);
	}

}
