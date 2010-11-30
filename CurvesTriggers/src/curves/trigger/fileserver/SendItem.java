package curves.trigger.fileserver;

import java.io.File;
import java.util.Hashtable;

import curves.main.Bot;
import curves.main.Profile;

public class SendItem {

	public Bot getBot() {
		return bot;
	}

	public File getFile() {
		return file;
	}

	public Hashtable<String, Object> getStorage() {
		return storage;
	}

	public Profile getUser() {
		return user;
	}

	private Bot bot;
	private File file;
	private Hashtable<String, Object> storage;
	private Profile user;

	public SendItem(File file, Profile user, Bot bot,
			Hashtable<String, Object> storage) {
		this.file = file;
		this.storage = storage;
		this.bot = bot;
		this.user = user;
	}
	
	public static SendItem newItem(File file, Profile user, Bot bot,
			Hashtable<String, Object> storage){
		if (!file.exists()) return null;
		return new SendItem(file, user, bot, storage);
	}
	
	public boolean equals(Object o){
		if (o == null) return false;
		if (! (o instanceof SendItem)) return false;
		SendItem oi = (SendItem) o;
		return (oi.file.equals(this.file) && oi.user.equals(this.user));
	}
}
