package curves.trigger.mana;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.mysql.jdbc.PreparedStatement;

import curves.main.Bot;
import curves.main.Profile;

public abstract class LibMana {
	
	static Logger log = Logger.getLogger(LibMana.class);
	
	static int getMana(Profile user, Bot bot) {
		int ret;
		try {
			PreparedStatement ps = (PreparedStatement) bot.getDB()
					.prepareStatement("SELECT mana FROM mana WHERE user = ?;");
			ps.setString(1, user.getNickname());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				ret = rs.getInt("mana");
			} else {
				ret = -1;
			}
			ps.close();
			return ret;
		} catch (SQLException e) {
			log.error("Unable to get mana from the database.");
		}
		return 0;
	}

	static boolean payMana(int mana, Profile user, Bot bot) {
		if (getMana(user, bot) < mana)
			return false;
		try {
			PreparedStatement ps = (PreparedStatement) bot.getDB()
					.prepareStatement(
							"UPDATE mana SET mana = mana - ?"
									+ " WHERE user = ?;");
			ps.setInt(1, mana);
			ps.setString(2, user.getNickname());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			log.error("Unable to draw mana from the database.", e);
		}
		return true;
	}

	public static void newPlayer(Profile user, Bot bot) {
		try {
			PreparedStatement ps = (PreparedStatement) bot
					.getDB()
					.prepareStatement(
							"INSERT INTO mana (user, mana)"
									+ "VALUES (?, 1) ON DUPLICATE KEY UPDATE mana = 1;");
			ps.setString(1, user.getNickname());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			log.error("Unable to save new player into the database.", e);
		}
	}


}
