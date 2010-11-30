package curves.trigger.record;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.mysql.jdbc.PreparedStatement;

import curves.main.Bot;
import curves.message.IMessage;
import curves.message.PrivMsg;
import curves.trigger.IReadHandler;
import curves.trigger.lib.R_Whois;

public class R_NewMail implements IReadHandler {
	
	Logger log = Logger.getLogger(R_NewMail.class);

	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(PrivMsg.class);
	}

	public void process(IMessage message, Bot bot,
			Hashtable<String, Object> storage) {
		PrivMsg msg = (PrivMsg) message;
		String sender = msg.getUser().getNickname();
		if (!R_Whois.getInfo(msg.getUser(), bot).isIdentified()){
			bot.send(new PrivMsg(sender, "you need to be registered!"));
			return;
		}
		Pattern p = Pattern.compile("!message (.*?):(.*)");
		Matcher m = p.matcher(msg.getMessage());
		if (!m.find()) {
			bot.send(new PrivMsg(sender, "syntax error!"));
			return;
		}
		String receipient = m.group(1);
		if (receipient.length() > 64) {
			bot.send(new PrivMsg(sender, "receipient's nickname is too long!"));
			return;
		}
		String msgtext = m.group(2);
		if (msgtext.equals("") || msgtext == null){
			bot.send(new PrivMsg(sender, "there is no message body!"));
			return;
		}
		try {
			PreparedStatement ps = (PreparedStatement) bot.getDB()
					.prepareStatement(
							"INSERT INTO curves.mail (receipient, sender, message, time) "
									+ "VALUES (?, ?, ?, ?) "
									+ "ON DUPLICATE KEY UPDATE "
									+ "message = ?, time = ?;");
			ps.setString(1, receipient);
			ps.setString(2, sender);
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			ps.setString(4, dateFormat.format(new Date()));
			ps.setString(6, dateFormat.format(new Date()));
			byte[] msg_ = msgtext.getBytes("UTF-8");
			ps.setString(3, new String(msg_, "utf8"));
			ps.setString(5, new String(msg_, "utf8"));
			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			log.error("Unable to record mail to database.", e);
		}
	}

	public boolean reactsTo(IMessage message, Bot bot,
			Hashtable<String, Object> storage) {
		return ((PrivMsg) message).getMessage().startsWith("!message ");
	}

}
