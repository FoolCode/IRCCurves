package curves.message;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class for the PING message.
 * IRC: PING :<target>
 */

public class PingMsg extends IMessage {

	String target;

	public String getTarget() {
		if (target == null) {
			return "";
		}
		return target;
	}

	/**
	 * parse incoming PING message
	 * @param message
	 */
	public PingMsg(String message) {
		super(message);
		if (message == null) return;
		Pattern p = Pattern.compile("^PING :(.*?)$", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(original);
		if (m.find()) {
			this.target = m.group(1).split(" ")[0];
		}
	}

	/**
	 * create outgoing PING message
	 * @param target
	 * @param t nothing, just to differentiate from the other constructor by method signature
	 */
	public PingMsg(String target, boolean t) {
		super(null);
		this.target = target;
	}

	public String _toString() {
		return "PING :" + this.target;
	}
	
	public String getCommand() {
		return "PING";
	}

}
