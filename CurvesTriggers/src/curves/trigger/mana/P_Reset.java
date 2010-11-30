package curves.trigger.mana;

import java.sql.SQLException;
import java.util.Hashtable;

import org.apache.log4j.Logger;

import com.mysql.jdbc.PreparedStatement;

import curves.main.Bot;
import curves.trigger.IPeriodicHandler;

/**
 * Reset mana on start up.
 */
public class P_Reset implements IPeriodicHandler {
	
	Logger log = Logger.getLogger(P_Reset.class);
	
	public void process(Bot bot, Hashtable<String, Object> storage) {
		try {
			PreparedStatement ps = (PreparedStatement) bot.getDB()
					.prepareStatement("DELETE FROM mana;");
			ps.executeUpdate();
			ps.close();
			storage.put("mana reset", 1);
		} catch (SQLException e) {
			log.error("Unable to reset mana in the database.", e);
		}
	}

	public boolean isReady(Bot bot, Hashtable<String, Object> storage) {
		return !storage.containsKey("mana reset");
	}

}
