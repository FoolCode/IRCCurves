package curves.message;

import java.lang.reflect.Constructor;
import java.util.Enumeration;
import java.util.Hashtable;

import org.apache.log4j.Logger;

import curves.main.Bot;
import curves.main.Profile;

/**
 * Parent to all message types and includes certain basic features.
 */
@SuppressWarnings("unchecked")
public abstract class IMessage {
	/**
	 * original incoming message
	 */
	String original;

	static Logger log = Logger.getLogger(IMessage.class);

	/**
	 * @return in case of outgoing message, the created message
	 * @return in case of incoming message, the original message
	 */
	public String getOriginal() {
		if (original != null) {
			return original;
		} else {
			return toString();
		}
	}

	/**
	 * a message usually comes with a user;
	 */
	Profile user;

	public Profile getUser() {
		return user;
	}

	public boolean isFromAdminOf(Bot bot) {
		if (user == null)
			return false;
		Profile[] admins = bot.getAdmins();
		if (admins == null)
			return true;
		for (Profile admin : admins) {
			if (user.equals(admin))
				return true;
		}
		return false;
	}

	static Hashtable<String, Class> commands = initCommands();

	/**
	 * initialize the commands hash table the commands are fed into this hash
	 * table so a quick command to class mapping is possible when parsing the
	 * lines from IRC socket using those classes, actual instances of the
	 * messages can be constructed and passed on for further processing
	 * 
	 * @return commands hash table
	 */
	private static Hashtable<String, Class> initCommands() {
		Hashtable<String, Class> commands = new Hashtable<String, Class>();
		addCommand(commands, ErrorMsg.class);
		addCommand(commands, InviteMsg.class);
		addCommand(commands, JoinMsg.class);
		addCommand(commands, KickMsg.class);
		addCommand(commands, ModeMsg.class);
		addCommand(commands, NamesMsg.class);
		addCommand(commands, NamesReplyMsg.class);
		addCommand(commands, NamesEndMsg.class);
		addCommand(commands, NickMsg.class);
		addCommand(commands, NoticeMsg.class);
		addCommand(commands, PartMsg.class);
		addCommand(commands, PingMsg.class);
		addCommand(commands, PongMsg.class);
		addCommand(commands, PrivMsg.class);
		addCommand(commands, QuitMsg.class);
		addCommand(commands, TopicMsg.class);
		addCommand(commands, UserMsg.class);
		addCommand(commands, WhoisMsg.class);
		addCommand(commands, WhoisChannelsMsg.class);
		addCommand(commands, WhoisNameMsg.class);
		addCommand(commands, WhoisIdentifiedMsg.class);
		addCommand(commands, WhoisEndMsg.class);
		addCommand(commands, Generic.class);

		return commands;
	}

	public synchronized static Class[] getCommands() {
		Class[] classes = new Class[commands.size()];
		Enumeration<Class> e = commands.elements();
		for (int i = 0; e.hasMoreElements(); i++) {
			classes[i] = e.nextElement();
		}
		return classes;
	}

	private static void addCommand(Hashtable<String, Class> commands,
			Class messageClass) {
		Constructor<IMessage> constructor;
		try {
			constructor = messageClass
					.getConstructor(new Class[] { String.class });
			IMessage message = (IMessage) constructor
					.newInstance(new Object[] { null });
			commands.put(message.getCommand(), messageClass);
		} catch (Exception e) {
			log.error("Unable to add command " + messageClass.getClass(), e);
		}

	}

	/**
	 * to be implemented by every sub-class which is the output that will be
	 * sent to IRC, this will however be wrapped into an toString() here that
	 * cuts the message to the length of 512
	 */
	public abstract String _toString();

	public String toString() {
		String ret = _toString();
		if (ret.length() > 512)
			return ret.substring(0, 512);
		return ret;
	}

	/**
	 * @return the IRC command the sub-class is responsible for.
	 */
	public abstract String getCommand();

	/**
	 * this constructor is inherited and used by sub-classes to store the
	 * unparsed original message received from IRC.
	 * 
	 * @param message
	 */
	public IMessage(String message) {
		if (message == null)
			return;
		if (message.length() > 512) {
			this.original = message.substring(0, 511);
		} else {
			this.original = message;
		}
	}

	/**
	 * parses the input string received from IRC and switches according to
	 * message type
	 * 
	 * @param message
	 * @return instance of parsed sub-class
	 */
	public static IMessage parseMessage(String message) {
		// second part of a message is the command
		String command;
		if (message.startsWith(":")) {
			command = message.split(" ")[1].toUpperCase();
		} else {
			command = message.split(" ")[0].toUpperCase();
		}
		try {
			// depending on the command, get the right class and invoke an
			// object
			if (commands.containsKey(command)) {
				Constructor<IMessage> constructor;
				constructor = commands.get(command).getConstructor(
						new Class[] { String.class });
				return (IMessage) constructor
						.newInstance(new Object[] { message });
			} else {
				// unless that command does not exist
				return new Generic(message);
			}
		} catch (Exception e) {
			log.error("Unable to parse message: " + message, e);
			return null;
		}
	}

}
