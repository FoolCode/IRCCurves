package curves.message;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import curves.main.Profile;

/**
 * Quit message.
 */
public class QuitMsg extends IMessage {

	String message;

	public String getMessage() {
		return message;
	}

	public QuitMsg(String message) {
		super(message);
		if (message == null)
			return;
		Pattern p = Pattern.compile("^:(.*?) QUIT( :(.*?))?$",
				Pattern.CASE_INSENSITIVE);
		Matcher m = p
				.matcher(original);
		if (m.find()) {
			this.user = new Profile(m.group(1));
			this.message = m.group(3);
		}
	}

	public String _toString() {
		return "QUIT" + ((message == null) ? "" : (" :" + message));
	}

	public String getCommand() {
		return "QUIT";
	}

}
