package curves.message;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import curves.main.Profile;

/**
 * Pong message (reply to Ping).
 */
public class PongMsg extends IMessage {
	
	String target;
	static final String commandString = "PONG";
	
	public String getTarget() {
		if (target == null) {
			return "";
		}
		return target;
	}

	/**
	 * parse incoming PONG message
	 * TODO: no need for parsing incoming PONG messages so far
	 * @param message
	 */
	public PongMsg(String message) {
		super(message);
		if (message == null) return;
		Pattern p = Pattern.compile("^:(.*?) PONG .*? :(.*?)$", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(original);
		if (m.find()) {
			this.user = new Profile(m.group(1));
			this.target = m.group(2);
		}
	}

	/**
	 * create outgoing PONG message
	 * @param target
	 * @param t nothing, just to differentiate from the other constructor by method signature
	 */
	public PongMsg(String target, boolean t) {
		super(null);
		this.target = target;
	}

	public String _toString() {
		return "PONG :" + this.target;
	}
	
	public String getCommand() {
		return "PONG";
	}

}
