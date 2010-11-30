package curves.trigger.record;

import java.sql.SQLException;
import java.util.Hashtable;

import org.apache.log4j.Logger;

import com.mysql.jdbc.PreparedStatement;

import curves.main.Bot;
import curves.trigger.IPeriodicHandler;

public class P_PurgeMails implements IPeriodicHandler {

	int skip = 0;
	Logger log = Logger.getLogger(P_PurgeMails.class);
	
	public boolean isReady(Bot bot, Hashtable<String, Object> storage) {
		skip = (skip + 1) % 1440;
		return skip == 0;
	}

	public void process(Bot bot, Hashtable<String, Object> storage) {
		try {
			PreparedStatement ps = (PreparedStatement) bot
					.getDB()
					.prepareStatement(
							"DELETE FROM mail WHERE "
							+ "date( time ) < subdate( curdate( ) , INTERVAL 1 week );");
			ps.executeUpdate();
			ps.close();
			
		} catch (SQLException e) {
			log.error("Unable to purge mails.", e);
		}
	}

}
