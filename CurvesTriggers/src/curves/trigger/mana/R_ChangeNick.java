package curves.trigger.mana;

import java.sql.SQLException;
import java.util.Hashtable;

import org.apache.log4j.Logger;

import com.mysql.jdbc.PreparedStatement;

import curves.main.Bot;
import curves.message.IMessage;
import curves.message.NickMsg;
import curves.trigger.IReadHandler;

public class R_ChangeNick implements IReadHandler {
	
	Logger log = Logger.getLogger(R_ChangeNick.class);

	public void process(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		try {
			PreparedStatement ps = (PreparedStatement) bot.getDB()
					.prepareStatement("UPDATE mana SET user = ? WHERE user = ?;");
			ps.setString(1, ((NickMsg) message).getProfile().getNickname());
			ps.setString(2, message.getUser().getNickname());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			log.error("Unable to update player name in the database.", e);
		}
	}

	public boolean reactsTo(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		return true;
	}
	
	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(NickMsg.class);
	}

}
