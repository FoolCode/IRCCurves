package curves.trigger.record;

import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.mysql.jdbc.PreparedStatement;

import curves.main.Bot;
import curves.message.IMessage;
import curves.message.JoinMsg;
import curves.message.PrivMsg;
import curves.trigger.IReadHandler;

public class R_DeliverMail implements IReadHandler {
	
	Logger log = Logger.getLogger(R_DeliverMail.class);

	public boolean messageType(Class<IMessage> messageType) {
		return messageType.equals(JoinMsg.class);
	}

	public void process(IMessage message, Bot bot,
			Hashtable<String, Object> storage) {
		try {
			PreparedStatement ps = (PreparedStatement) bot.getDB()
					.prepareStatement(
							"SELECT * FROM mail WHERE receipient = ?;");
			String receipient =  ((JoinMsg) message).getUser().getNickname();
			ps.setString(1, receipient);
			ResultSet rs = ps.executeQuery();
			LinkedList<String> messages = new LinkedList<String>();
			while(rs.next()){
				String msgtext = rs.getString("sender");
				msgtext += ": ";
				msgtext += rs.getString("message");
				messages.add(msgtext);
			}
			ps.close();
			new SendQueue(messages, bot, receipient).start();
			ps = (PreparedStatement) bot.getDB()
				.prepareStatement(
					"DELETE FROM mail WHERE receipient = ?;");
			ps.setString(1, receipient);
			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			log.error("Unable to access mails in the database.", e);
		}
	}

	public boolean reactsTo(IMessage message, Bot bot,
			Hashtable<String, Object> storage) {
		return true;
	}

	class SendQueue extends Thread{

		private LinkedList<String> messages;
		private Bot bot;
		private String receipient;

		public SendQueue(LinkedList<String> messages, Bot bot, String receipient){
			this.messages = messages;
			this.bot = bot;
			this.receipient = receipient;
		}
		
		public void run() {
			Iterator<String> it = messages.iterator();
			while (it.hasNext()){
				bot.send(new PrivMsg(receipient, it.next()));
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
}
