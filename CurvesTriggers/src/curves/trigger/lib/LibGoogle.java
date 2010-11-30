package curves.trigger.lib;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.json.JSONObject;

public abstract class LibGoogle {
	public static JSONObject googleQuery(String type, String query)
			throws Exception {
		URL url = new URL("http://ajax.googleapis.com/ajax/services/search/"
				+ type + "?v=1.0&q=" + URLEncoder.encode(query, "UTF-8"));
		URLConnection connection = url.openConnection();
		String line;
		StringBuilder builder = new StringBuilder();
		BufferedReader reader;
		reader = new BufferedReader(new InputStreamReader(connection
				.getInputStream(), "UTF-8"));
		while ((line = reader.readLine()) != null) {
			builder.append(line);
		}
		return new JSONObject(builder.toString());
	}
}
