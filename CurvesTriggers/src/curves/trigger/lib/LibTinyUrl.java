package curves.trigger.lib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;

public abstract class LibTinyUrl {
	public static String shorten(String fullUrl) throws HttpException,
			IOException {
		HttpClient httpclient = new HttpClient();
		// Prepare a request object
		HttpMethod method = new GetMethod("http://tinyurl.com/api-create.php");
		method.setQueryString(new NameValuePair[] { new NameValuePair("url",
				fullUrl) });
		httpclient.executeMethod(method);
		BufferedReader reader = new BufferedReader(new InputStreamReader(method
				.getResponseBodyAsStream(), "UTF-8"));
		String line;
		StringBuilder builder = new StringBuilder();
		while ((line = reader.readLine()) != null) {
			builder.append(line);
		}
		String tinyUrl = builder.toString();
		method.releaseConnection();
		return tinyUrl;
	}
}