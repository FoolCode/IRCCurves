package curves.trigger.query;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

import org.apache.log4j.Logger;

import com.mysql.jdbc.PreparedStatement;

import curves.main.Bot;
import curves.message.IMessage;
import curves.message.PrivMsg;
import curves.trigger.IReadHandler;

public class R_Find implements IReadHandler {
	
	Logger log = Logger.getLogger(R_Find.class);

	public void process(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		try {
			PrivMsg msg = (PrivMsg) message;
			String text = ((PrivMsg) message).trailing();
			PreparedStatement ps = (PreparedStatement) bot
					.getDB()
					.prepareStatement(
							"SELECT count(*) AS c FROM messages WHERE MATCH(message, user)"
									+ " AGAINST(? IN BOOLEAN MODE) AND target = ? AND user != ?"
									+ " AND substr(message, 1, 1) != '!' AND char_length(message) > 10");
			int random = 0;
			ps.setString(1, text);
			ps.setString(2, msg.getTarget());
			ps.setString(3, bot.getProfile().getNickname());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				int count = rs.getInt("c");
				if (count == 0) {
					msg.reply(bot, "I didn't find anything containing \""
							+ text + "\"");
					return;
				}
				random = (int) Math.floor((Math.random() * count));
			}
			ps.close();
			ps = (PreparedStatement) bot
					.getDB()
					.prepareStatement(
							"SELECT user, message, date_format(time,'%d %b %Y at %H:%i') AS times"
									+ " FROM messages WHERE MATCH(message, user) AGAINST(? IN BOOLEAN MODE)"
									+ " AND target = ? AND user != ? AND substr(message, 1, 1) != '!'"
									+ " AND char_length(message) > 10 LIMIT ?,1");
			ps.setString(1, text);
			ps.setString(2, msg.getTarget());
			ps.setString(3, bot.getProfile().getNickname());
			ps.setInt(4, random);
			rs = ps.executeQuery();
			if (rs.next()) {
				String quote = rs.getString("message");
				if (quote.startsWith("ACTION"))
					quote = quote.replaceFirst("ACTION", "/me");
				String time = rs.getString("times");
				String user = rs.getString("user");
				msg.reply(bot, user + " said on " + time + ": \"" + quote
						+ "\"");
			}
			ps.close();
		} catch (SQLException e) {
			log.error("Unable to fetch records from the database.", e);
		}
	}

	public boolean reactsTo(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		return ((PrivMsg) message).getMessage().startsWith("!find ")
				&& ((PrivMsg) message).getTarget().startsWith("#");
	}

	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(PrivMsg.class);
	}

}
