package curves.trigger.record;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

import org.apache.log4j.Logger;

import com.mysql.jdbc.PreparedStatement;

import curves.main.Bot;
import curves.message.IMessage;
import curves.message.PrivMsg;
import curves.trigger.IReadHandler;
import curves.trigger.IWriteHandler;

public class W_PrivMsg implements IReadHandler, IWriteHandler {
	
	Logger log = Logger.getLogger(W_PrivMsg.class);

	public void process(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		PrivMsg msg = (PrivMsg) message;
		if (msg.getTarget().equals("NickServ")) return;
		try {
			PreparedStatement ps = (PreparedStatement) bot.getDB()
					.prepareStatement(
							"INSERT INTO curves.messages (target, user, time, message)"
									+ "VALUES (?, ?, ?, ?);");
			ps.setString(1, msg.getTarget());
			ps.setString(2, bot.getProfile().getNickname());
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			ps.setString(3, dateFormat.format(new Date()));
			byte[] msg_ = msg.getMessage().getBytes("UTF-8");
			ps.setString(4, new String(msg_, "utf8"));
			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			log.error("Unable to record message to database.", e);
		}
	}

	public boolean reactsTo(IMessage message, Bot bot, Hashtable<String, Object> storage) {
		return true;
	}
	
	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(PrivMsg.class);
	}

}
