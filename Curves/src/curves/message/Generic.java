package curves.message;

/**
 * Used for unknown message commands and to send out raw commands.
 */
public class Generic extends IMessage {

	public Generic(String message) {
		super(message);
		if (message == null)
			return;
	}

	public String _toString() {
		return getOriginal();
	}

	public String getCommand() {
		return "_GENERIC_";
	}

}
