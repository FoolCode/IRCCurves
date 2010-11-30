package curves.main;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import com.mysql.jdbc.Connection;

public class Main {

	static LinkedList<Bot> botList;
	static Logger log;

	public static void main(String[] args) {
		
		botList = new LinkedList<Bot>();
		
		if (args.length < 2) {
			System.err.println("don't forget to specify config files!");
			return;
		}

		// load log4j config
		DOMConfigurator.configure(args[1]);
		log = Logger.getLogger(Main.class);
		
		// load config file and prepare bots
		loadConfigFile(args[0]);
		
		// run all bots
		Iterator<Bot> iterator = botList.iterator();
		Bot next;
		while (iterator.hasNext()) {
			next = iterator.next();
			next.start();
			log.debug(next.getProfile() + " on "+ next.getServer() + " started.");
		}
	}

	@SuppressWarnings("unchecked")
	private static void loadConfigFile(String configFile) {
		// parse configuration file
		try {
			XMLConfiguration config = new XMLConfiguration(configFile);
			List<HierarchicalConfiguration> bots = config
					.configurationsAt("bot");
			
			for (Iterator<HierarchicalConfiguration> ibots = bots.iterator(); ibots
					.hasNext();) {
				HierarchicalConfiguration bot = ibots.next();
				// parse profile information
				Profile profile = new Profile(bot.getString("nickname"), bot
						.getString("hostname"), bot.getString("servername"),
						bot.getString("realname"), bot.getString("nickserv"));

				parseChannels(bot, profile);
				
				Connection database = null;
				if (bot.getString("database.url") != null)
					database = (Connection) DriverManager.getConnection(bot
							.getString("database.url"), bot
							.getString("database.username"), bot
							.getString("database.password"));

				Bot newBot = new Bot(bot.getString("server"), bot
						.getInt("port"), profile, bot.getLong("period"),
						database);

				newBot.setAdmins(parseAdmins(bot));

				// parse listeners
				List<HierarchicalConfiguration> indexes = bot
						.configurationsAt("listeners.index");
				for (Iterator<HierarchicalConfiguration> iind = indexes
						.iterator(); iind.hasNext();) {
					HierarchicalConfiguration index = iind.next();
					String critical = index.getString("critical");
					newBot.addListener(new MessageListener(index
							.getString("classpath"), index
							.getString("classname"), critical != null
							&& critical.equals("true"), index.getString("properties")));
				}

				botList.add(newBot);
			}
		} catch (ConfigurationException e) {
			log.error("Something is wrong with the configuration", e);
		} catch (SQLException e) {
			log.error("Initializing database failed", e);
		}
	}

	@SuppressWarnings("unchecked")
	private static ArrayList<Profile> parseAdmins(HierarchicalConfiguration bot) {
		// parse admins
		List<HierarchicalConfiguration> admins = bot
				.configurationsAt("admins.admin");
		ArrayList<Profile> adminList = new ArrayList<Profile>();
		for (Iterator<HierarchicalConfiguration> iadm = admins.iterator(); iadm
				.hasNext();) {
			HierarchicalConfiguration admin = iadm.next();
			adminList.add(new Profile(admin.getString("nickname"), admin
					.getString("hostname")));
		}
		return adminList;
	}

	@SuppressWarnings("unchecked")
	private static ArrayList<Channel> parseChannels(
			HierarchicalConfiguration bot, Profile profile) {
		// parse initial channels
		List<String> channels = bot.getList("channels.channel");
		ArrayList<Channel> channelList = new ArrayList<Channel>();
		for (Iterator<String> ichans = channels.iterator(); ichans.hasNext();) {
			String channelname = ichans.next();
			profile.addChannel(new Channel(channelname), 0);
		}
		return channelList;
	}
}
