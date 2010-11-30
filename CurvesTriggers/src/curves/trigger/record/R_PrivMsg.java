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

public class R_PrivMsg implements IReadHandler, IWriteHandler {

	Logger log = Logger.getLogger(R_PrivMsg.class);

	public void process(IMessage message, Bot bot,
			Hashtable<String, Object> storage) {
		PrivMsg msg = (PrivMsg) message;
		try {
			PreparedStatement ps = (PreparedStatement) bot.getDB()
					.prepareStatement(
							"INSERT INTO curves.messages (target, user, time, message)"
									+ "VALUES (?, ?, ?, ?);");
			ps.setString(1, trim(msg.getTarget(), 64));
			ps.setString(2, trim(msg.getUser().getNickname(), 64));
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			ps.setString(3, dateFormat.format(new Date()));
			byte[] msg_ = trim(msg.getMessage(), 512).getBytes("UTF-8");
			ps.setString(4, new String(msg_, "utf8"));
			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			log.error("Unable to record message to database.", e);
		}
	}

	public boolean reactsTo(IMessage message, Bot bot,
			Hashtable<String, Object> storage) {
		return (message.getUser() != null)
				&& !message.getUser().getNickname().equals("LagServ")
				&& !message.getUser().getNickname().equals("PingServ");
	}

	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(PrivMsg.class);
	}

	private String trim(String source, int length) {
		if (source == null)
			return null;
		if (source.length() <= length)
			return source;
		return source.substring(0, length);
	}

}
