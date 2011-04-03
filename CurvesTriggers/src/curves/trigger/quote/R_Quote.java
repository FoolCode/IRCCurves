package curves.trigger.quote;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

import org.apache.log4j.Logger;

import com.mysql.jdbc.PreparedStatement;

import curves.main.Bot;
import curves.message.IMessage;
import curves.message.PrivMsg;
import curves.trigger.IReadHandler;

public class R_Quote implements IReadHandler {
	
	Logger log = Logger.getLogger(R_Quote.class);

	public void process(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		PrivMsg msg = (PrivMsg) message;
		String somebody = ((PrivMsg) message).trailing();
		int random;
		try {
			PreparedStatement ps;
			if (!somebody.equals("")) {
				ps = (PreparedStatement) bot
						.getDB()
						.prepareStatement(
								"SELECT COUNT(*) AS c FROM messages WHERE user=?"
										+ " AND target = ? AND substr(message, 1, 1) != '!'"
										+ " AND char_length(message) > 20"
								);
				ps.setString(1, somebody);

			} else {
				ps = (PreparedStatement) bot
						.getDB()
						.prepareStatement(
								"SELECT COUNT(*) AS c FROM messages WHERE user!=?"
										+ " AND target = ? AND substr(message, 1, 1) != '!'"
										+ " AND char_length(message) > 20");
				ps.setString(1, bot.getProfile().getNickname());
			}
			ps.setString(2, msg.getTarget());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				int count = rs.getInt("c");
				if (count == 0) {
					msg.reply(bot, somebody
							+ " didn't say anything worth mentioning.");
					return;
				}
				random = (int) Math.floor((Math.random() * count));
			} else {
				return;
			}
			ps.close();
			if (!somebody.equals("")) {
				ps = (PreparedStatement) bot.getDB().prepareStatement(
						"SELECT user, message, date_format(time,'%d %b %Y at %H:%i') AS times"
								+ " FROM messages WHERE user=? AND target = ?"
								+ " AND substr(message, 1, 1) != '!'"
								+ " AND char_length(message) > 20 LIMIT ?,1");
				ps.setString(1, somebody);
			} else {
				ps = (PreparedStatement) bot
						.getDB()
						.prepareStatement(
								"SELECT user, message, date_format(time,'%d %b %Y at %H:%i') AS times"
										+ " FROM messages WHERE user!=? AND target = ?"
										+ " AND date( time ) > subdate( curdate( ) , INTERVAL 1 month )"
										+ " AND substr(message, 1, 1) != '!' AND char_length(message) > 20 LIMIT ?,1");
				ps.setString(1, bot.getProfile().getNickname());
			}
			ps.setString(2, msg.getTarget());
			ps.setInt(3, random);
			rs = ps.executeQuery();
			if (rs.next()) {
				String quote = rs.getString("message");
				if (quote.startsWith("ACTION"))
					quote = quote.replaceFirst("ACTION", "/me");
				String time = rs.getString("times");
				msg.reply(bot, rs.getString("user") + " said on " + time
						+ ": \"" + quote + "\"");
			}
			ps.close();
		} catch (SQLException e) {
			log.error("Unable to fetch records from the database.", e);
		}
	}

	public boolean reactsTo(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		PrivMsg msg = (PrivMsg) message;
		if (!msg.getTarget().startsWith("#"))
			return false;
		return msg.getMessage().startsWith("!cite")
				|| msg.getMessage().startsWith("!quote");
	}
	
	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(PrivMsg.class);
	}

}
