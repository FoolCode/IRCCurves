package curves.message;

import curves.main.Channel;

public class NamesMsg extends IMessage {
	
	Channel channel;
	
	public Channel getChannel(){
		return channel;
	}
	
	/**
	 * there wont be any incoming NAMES commands!
	 */
	public NamesMsg(String message) {
		super(message);
		if (message == null) return;
	}

	public NamesMsg(Channel channel){
		super(null);
		this.channel = channel;
	}
	
	public String _toString() {
		return "NAMES "+channel;
	}
	
	public String getCommand(){
		return "NAMES";
	}

}
