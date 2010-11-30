package curves.trigger.fileserver;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.regex.Pattern;

import curves.main.Bot;
import curves.main.Channel;
import curves.main.Profile;
import curves.message.IMessage;
import curves.message.PrivMsg;
import curves.trigger.IReadHandler;
import curves.trigger.lib.MainChannel;
import curves.trigger.lib.R_Whois;

public class R_Request implements IReadHandler {

	P_Queue queue;

	public R_Request(P_Queue queue) {
		this.queue = queue;
	}

	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(PrivMsg.class);
	}

	public void process(IMessage message, Bot bot,
			Hashtable<String, Object> storage) {
		Profile profile = R_Whois.getInfo(message.getUser(), bot);
		boolean isInMainChannel = false;
		Channel mainChannel = MainChannel.get(storage);
		for(Channel chan: profile.getChannels()){
			if (chan.equals(mainChannel)){
				isInMainChannel = true;
			}
		}
		if (!isInMainChannel){
			bot.send(new PrivMsg(message.getUser().getNickname(), "You have to join " + mainChannel + " in order to download!"));
			return;
		}
		String realname = profile.getRealname();
		if (realname.toLowerCase().contains("webirc")){
			bot.send(new PrivMsg(message.getUser().getNickname(), (String) storage.get("webirc message")));
			return;
		}
		String[] terms = ((PrivMsg) message).trailing().split(" |_|-|#");
		if (terms.length == 1
				&& (terms[0].equals("latest") || terms[0].equals("newest"))) {
			String latestFile = (String) storage.get("latest file");
			addToQueue(latestFile, storage, bot, message.getUser());
			return;
		}
		ArrayList<String> found = find(terms, storage);
		if (found.size() < 1) {
			bot.send(new PrivMsg(message.getUser().getNickname(),
					"I didnt find any file related to \""
							+ ((PrivMsg) message).trailing() + "\"."));
		} else if (found.size() > 3) {
			bot.send(new PrivMsg(message.getUser().getNickname(),
					"I found too many files: \"" + found.get(0) + "\", \""
							+ found.get(1)
							+ ", etc... please be more specific."));
		} else if (found.size() > 1) {
			String files = "";
			int length = 10000;
			String shortest = "";
			for (int i = 0; i < found.size(); i++) {
				files = files + "\"" + found.get(i) + "\", ";
				if (found.get(i).length() < length) {
					shortest = found.get(i);
					length = found.get(i).length();
				}
			}
			bot.send(new PrivMsg(message.getUser().getNickname(),
					"I found several files: " + files + "etc."));
			addToQueue(shortest, storage, bot, message.getUser());
		} else {
			addToQueue(found.get(0), storage, bot, message.getUser());
		}

	}

	private synchronized void addToQueue(String file,
			Hashtable<String, Object> storage, Bot bot, Profile user) {
		String root = (String) storage.get("file root");
		if (queue.add(SendItem.newItem(new File(root + file), user, bot,
				storage))) {
			bot.send(new PrivMsg(user.getNickname(), file
					+ " was added to your download queue."));
		} else {
			bot
					.send(new PrivMsg(user.getNickname(), file
							+ " was NOT queued."));
		}

	}

	public boolean reactsTo(IMessage message, Bot bot,
			Hashtable<String, Object> storage) {
		return (((PrivMsg) message).getMessage().startsWith("!giveme ")
						|| ((PrivMsg) message).getMessage().startsWith("!get ") || ((PrivMsg) message)
						.getMessage().startsWith("!download "));
	}

	@SuppressWarnings("unchecked")
	private ArrayList<String> find(String[] terms,
			Hashtable<String, Object> storage) {
		ArrayList<String> filelist = (ArrayList<String>) storage
				.get("file list");
		ArrayList<String> result = new ArrayList<String>();
		Pattern[] patterns = new Pattern[terms.length];
		for (int i = 0; i < patterns.length; i++) {
			patterns[i] = Pattern.compile(Pattern.quote(terms[i]),
					Pattern.CASE_INSENSITIVE);
		}
		boolean skip;
		for (String file : filelist) {
			skip = false;
			for (int i = 0; i < patterns.length; i++) {
				if (!patterns[i].matcher(file).find()) {
					skip = true;
					break;
				}
			}
			if (!skip) {
				// found a candidate
				result.add(file);
			}
		}
		return result;
	}
}
