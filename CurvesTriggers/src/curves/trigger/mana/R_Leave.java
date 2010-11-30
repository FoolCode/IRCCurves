package curves.trigger.mana;

import java.sql.SQLException;
import java.util.Hashtable;

import org.apache.log4j.Logger;

import com.mysql.jdbc.PreparedStatement;

import curves.main.Bot;
import curves.message.IMessage;
import curves.message.KickMsg;
import curves.message.PartMsg;
import curves.message.QuitMsg;
import curves.trigger.IReadHandler;
import curves.trigger.lib.MainChannel;

public class R_Leave implements IReadHandler {
	
	Logger log = Logger.getLogger(R_Leave.class);

	public void process(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		try {
			PreparedStatement ps = (PreparedStatement) bot.getDB()
					.prepareStatement("DELETE FROM mana WHERE user = ?;");
			ps.setString(1, message.getUser().getNickname());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			log.error("Unable to remove leaving player from the database.", e);
		}
	}

	public boolean reactsTo(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		return (message instanceof QuitMsg)
				|| (message instanceof PartMsg && MainChannel.in(message, storage))
				|| (message instanceof KickMsg && MainChannel.in(message, storage));
	}

	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(QuitMsg.class)
				|| messageType.equals(KickMsg.class)
				|| messageType.equals(PartMsg.class);
	}
}
