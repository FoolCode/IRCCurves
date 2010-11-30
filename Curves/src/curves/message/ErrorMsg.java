package curves.message;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Receive error messages from the server. You don't send out any error messages
 * by yourself!
 */
public class ErrorMsg extends IMessage {

	String error;

	public String getError() {
		return error;
	}

	public ErrorMsg(String message) {
		super(message);
		if (message == null)
			return;
		Pattern p = Pattern.compile("^ERROR :(.*)$", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(original);
		if (m.find()) {
			this.error = m.group(1).split(" ")[0];
		}
	}

	public String getCommand() {
		return "ERROR";
	}

	public String _toString() {
		return "ERROR :" + error;
	}

}
