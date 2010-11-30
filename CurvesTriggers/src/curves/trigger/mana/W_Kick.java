package curves.trigger.mana;

import java.sql.SQLException;
import java.util.Hashtable;

import org.apache.log4j.Logger;

import com.mysql.jdbc.PreparedStatement;

import curves.main.Bot;
import curves.message.IMessage;
import curves.message.KickMsg;
import curves.trigger.IWriteHandler;
import curves.trigger.lib.MainChannel;

public class W_Kick implements IWriteHandler {

	Logger log = Logger.getLogger(W_Kick.class);

	public void process(IMessage message, Bot bot,
			Hashtable<String, Object> storage) {
		try {
			PreparedStatement ps = (PreparedStatement) bot.getDB()
					.prepareStatement("DELETE FROM mana WHERE user = ?;");
			ps.setString(1, ((KickMsg) message).getTarget().getNickname());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			log.error("Unable to remove kicked player from the database.", e);
		}
	}

	public boolean reactsTo(IMessage message, Bot bot,
			Hashtable<String, Object> storage) {
		return MainChannel.in(message, storage);
	}

	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(KickMsg.class);
	}
}
