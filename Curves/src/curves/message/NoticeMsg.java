package curves.message;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import curves.main.Bot;
import curves.main.Profile;

/**
 * Notice message.
 */
public class NoticeMsg extends IMessage {

	String target;

	public String getTarget() {
		return target;
	}

	private String message;
	
	public String getMessage(){
		return message;
	}
	
	/**
	 * parse incoming NOTICE message
	 * 
	 * @param message
	 */
	public NoticeMsg(String message) {
		super(message);
		if (message == null)
			return;
		Pattern p = Pattern.compile("^:(.*?) NOTICE (.*?) :(.*)$",
				Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(original);
		if (m.find()) {
			this.user = new Profile(m.group(1));
			this.target = m.group(2);
			this.message = m.group(3);
		}
	}

	/**
	 * create outgoing NOTICE message
	 * 
	 * @param target
	 * @param message
	 */
	public NoticeMsg(String target, String message) {
		super(null);
		this.target = target;
		this.message = message;
	}

	/**
	 * reply to the user
	 * 
	 * @param bot
	 *            that should reply
	 * @param message
	 *            message to send out
	 */
	public void reply(Bot bot, String message) {
		bot.send(new NoticeMsg(target, message));
	}

	public String _toString() {
		return "NOTICE " + target + " :" + message;
	}

	public String getCommand() {
		return "NOTICE";
	}

}
