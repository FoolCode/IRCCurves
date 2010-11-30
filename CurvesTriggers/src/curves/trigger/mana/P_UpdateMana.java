package curves.trigger.mana;

import java.sql.SQLException;
import java.util.Hashtable;

import org.apache.log4j.Logger;

import com.mysql.jdbc.PreparedStatement;

import curves.main.Bot;
import curves.trigger.IPeriodicHandler;

public class P_UpdateMana implements IPeriodicHandler {

	int skip = 0;
	
	Logger log = Logger.getLogger(P_UpdateMana.class);

	public boolean isReady(Bot bot, Hashtable<String, Object> storage) {
		skip = (skip + 1) % 3;
		return skip == 0;
	}

	public void process(Bot bot, Hashtable<String, Object> storage) {
		try {
			PreparedStatement ps = (PreparedStatement) bot.getDB()
					.prepareStatement("UPDATE mana SET mana = mana + 1 WHERE mana < 10;");
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			log.error("Unable to update mana in the database.", e);
		}
	}

}
