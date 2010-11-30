package curves.main;

import java.util.Enumeration;
import java.util.Hashtable;

public class Profile {
	
	public static int VOICE = 2;
	public static int HALFOP = 4;
	public static int OP = 8;
	public static int ADMIN = 16;

	private String nickname = null;
	private String hostname = null;
	private String servername = null;
	private String realname = null;
	private String nickserv = null;
	private Hashtable<Channel, Integer> channels = null;
	private boolean identified = false;
	
	public void setIdentified(boolean id){
		identified = id;
	}
	
	public boolean isIdentified(){
		return identified;
	}
	
	public void addChannel(Channel channel, int priv){
		channels.put(channel, new Integer(priv));
	}
	
	public void removeChannel(Channel channel){
		channels.remove(channel);
	}
	
	public int getPriv(Channel channel){
		if (channels.containsKey(channel)){
			return channels.get(channel).intValue();
		} else {
			return 0;
		}
	}
	
	public Hashtable<Channel, Integer> getChannelTable(){
		return channels;
	}
	
	public static int parsePriv(String priv){
		int ret = 0;
		if (priv.contains("!") || priv.contains("~")) ret += ADMIN;
		if (priv.contains("@")) ret += OP;
		if (priv.contains("%")) ret += HALFOP;
		if (priv.contains("+")) ret += VOICE;
		return ret;
	}
	
	public synchronized Channel[] getChannels(){
		Channel[] ret = new Channel[channels.size()];
		Enumeration<Channel> e = channels.keys();
		for(int i = 0; i < ret.length && e.hasMoreElements(); i++){
			ret[i] = e.nextElement();
		}
		return ret;
	}

	public String getNickname() {
		return nickname;
	}

	public String getHostname() {
		if (hostname == null) {
			return "";
		}
		return hostname;
	}

	public String getServername() {
		if (servername == null) {
			return "";
		}
		return servername;
	}

	public void setRealName(String realname){
		this.realname = realname;
	}
	
	public String getRealname() {
		if (realname == null) {
			return "";
		}
		return realname;
	}

	public String getNickserv() {
		if (nickserv == null) {
			return "";
		}
		return nickserv;
	}

	private String trim(String source, int length) {
		if (source == null)
			return null;
		if (source.length() <= length)
			return source;
		return source.substring(0, length);

	}

	public Profile(String nickname, String hostname) {
		this.nickname = trim(nickname, 64);
		this.hostname = trim(hostname, 256);
		this.identified = false;
		this.channels = new Hashtable<Channel, Integer>();
	}

	public Profile(String nickname, String hostname, String servername,
			String realname, String nickserv) {
		this.nickname = trim(nickname, 64);
		this.hostname = trim(hostname, 256);
		this.servername = trim(servername, 256);
		this.realname = trim(realname, 256);
		this.nickserv = trim(nickserv, 256);
		this.identified = false;
		this.channels = new Hashtable<Channel, Integer>();
	}

	public Profile(String nickhost) {
		String[] userinfo = nickhost.split("!");
		this.nickname = trim(userinfo[0], 64);
		this.hostname = (userinfo.length > 1) ? trim(userinfo[1], 255) : null;
		this.identified = false;
		this.channels = new Hashtable<Channel, Integer>();
	}

	public String toString() {
		return (this.nickname + ((this.hostname == null) ? ""
				: ("|" + this.hostname)));
	}

	public boolean equals(Object o) {
		if (!(o instanceof Profile)) return false;
		Profile other = (Profile) o;
		
		if (other == null) return false;
		
		if (this.nickname == null || other.getNickname() == null)
			return false;
		
		if (!this.nickname.toLowerCase().equals(
				other.getNickname().toLowerCase()))
			return false;
		
		if (this.hostname == null || other.getHostname() == null)
			return true;

		if (this.hostname.toLowerCase().equals(other.hostname.toLowerCase()))
			return true;

		return false;
	}
	
	public int hashCode() {
		return nickname.hashCode();
	}

	public void mergeWith(Profile other){
		if (this.channels.isEmpty()) this.channels = other.getChannelTable();
		if (this.hostname == null) this.hostname = other.getHostname();
		if (this.nickname == null) this.nickname = other.getNickname();
		if (this.realname == null) this.realname = other.getRealname();
		if (this.servername == null) this.servername = other.getServername();
		this.identified = this.identified || other.isIdentified();
	}

	
	
}
