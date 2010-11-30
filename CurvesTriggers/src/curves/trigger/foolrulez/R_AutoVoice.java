package curves.trigger.foolrulez;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

import org.apache.log4j.Logger;

import com.mysql.jdbc.PreparedStatement;

import curves.main.Bot;
import curves.message.IMessage;
import curves.message.JoinMsg;
import curves.message.ModeMsg;
import curves.trigger.IReadHandler;
import curves.trigger.lib.MainChannel;

public class R_AutoVoice implements IReadHandler {
	
	Logger log = Logger.getLogger(R_AutoVoice.class);

	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(JoinMsg.class);
	}

	public void process(IMessage message, Bot bot,
			Hashtable<String, Object> storage) {
		try {
			JoinMsg msg = (JoinMsg) message;
			PreparedStatement ps = (PreparedStatement) bot
					.getDB()
					.prepareStatement(
							"SELECT count(*) AS c FROM messages WHERE user = ? AND target = ? "
									+ "AND date( time ) > subdate( curdate( ) , INTERVAL 1 week ) GROUP BY user;");
			ps.setString(1, msg.getUser().getNickname());
			ps.setString(2, msg.getChannel().getChannel());
			ResultSet rs = ps.executeQuery();
			if (!rs.next() || rs.getInt("c") < 30) {
				ps.close();
				return;
			}
			ps.close();
			bot.send(new ModeMsg(msg.getChannel(), "+v", msg.getUser()
					.getNickname()));
		} catch (SQLException e) {
			log.error("Unable to fetch history data from database.", e);
		}

	}

	public boolean reactsTo(IMessage message, Bot bot,
			Hashtable<String, Object> storage) {
		return MainChannel.in(message, storage);
	}

}
