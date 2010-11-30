package curves.main;

/**
 * This class represents an IRC channel
 */
public class Channel {
	
	String channel;
	
	public String getChannel(){
		return channel;
	}
	
	public Channel(String channel){
		if (!channel.startsWith("#")) channel = "#"+channel;
		this.channel = channel.toLowerCase();
		if (channel.length() > 64) channel = channel.substring(0, 63);
	}
	
	public String toString(){
		return this.channel;
	}
	
	public boolean equals(Object o){
		if (!(o instanceof Channel)) return false;
		return this.getChannel().equals(((Channel) o).getChannel());
	}

	public int hashCode() {
		return channel.hashCode();
	}
}
