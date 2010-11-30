package curves.message;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import curves.main.Bot;
import curves.main.Profile;

/**
 * Class for the PRIVMSG command IRC: PRIVMSG <list,of,targets> :<message>
 */

public class PrivMsg extends IMessage {

	private String target;
	
	private String message;
	
	public String getMessage(){
		return message;
	}

	public String getTarget() {
		return target;
	}

	/**
	 * parse incoming PRIVMSG message
	 * 
	 * @param message
	 */
	public PrivMsg(String message) {
		super(message);
		if (message == null)
			return;
		Pattern p = Pattern.compile("^:(.*?) PRIVMSG (.*?) :(.*)$",
				Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(original);
		if (m.find()) {
			this.user = new Profile(m.group(1));
			this.target = m.group(2);
			this.message = m.group(3);
		}
	}

	/**
	 * create outgoing PRIVMSG message
	 * 
	 * @param target
	 * @param message
	 */
	public PrivMsg(String target, String message) {
		super(null);
		this.target = target;
		this.message = message;
	}
	
	public String trailing(){
		Pattern p = Pattern.compile("^.*? (.*)$");
		Matcher m = p.matcher(message);
		if (m.find()) return m.group(1);
		return "";
	}

	/**
	 * reply to the user
	 * 
	 * @param bot
	 *            that should reply
	 * @param message
	 *            message to send out
	 */
	public void reply(Bot bot, String reply) {
		if (target.startsWith("#")){
			bot.send(new PrivMsg(target, reply));
		} else {
			bot.send(new PrivMsg(user.getNickname(), reply));
		}
	}

	public String _toString() {
		return "PRIVMSG " + target + " :" + message;
	}

	public String getCommand() {
		return "PRIVMSG";
	}

}
