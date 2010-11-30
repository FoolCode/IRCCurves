package curves.trigger.record;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;

import org.apache.log4j.Logger;

import com.mysql.jdbc.PreparedStatement;

import curves.main.Bot;
import curves.main.Profile;
import curves.message.IMessage;
import curves.message.NamesReplyMsg;
import curves.trigger.IReadHandler;

/**
 * Updates the uptime table, works together with P_QueryNames and R_FinishNames
 */
public class R_ReceiveNames implements IReadHandler {
	
	Logger log = Logger.getLogger(R_ReceiveNames.class);

	public void process(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		NamesReplyMsg nrm = (NamesReplyMsg) message;
		ArrayList<Profile> names = nrm.getNames();
		String channel = nrm.getChannel().toString();
		int period = (int) (60 * bot.getPeriodicTimer() / 1000);
		try {
			for (Profile name : names) {
				PreparedStatement ps = (PreparedStatement) bot
						.getDB()
						.prepareStatement(
								"INSERT INTO curves.uptime (user, channel, seconds)"
										+ "VALUES (?, ?, ?)"
										+ "ON DUPLICATE KEY UPDATE seconds = seconds + ?");
				ps.setString(1, name.getNickname());
				ps.setString(2, channel);
				ps.setInt(3, period);
				ps.setInt(4, period);
				ps.executeUpdate();
				ps.close();
			}
		} catch (SQLException e) {
			log.error("Unable to log users currently in the channel.", e);
		}
	}

	public boolean reactsTo(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		return storage.containsKey(
				"uptimes " + ((NamesReplyMsg) message).getChannel());
	}

	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(NamesReplyMsg.class);
	}

}
