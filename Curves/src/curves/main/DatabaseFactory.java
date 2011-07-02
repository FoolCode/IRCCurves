package curves.main;

import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.mysql.jdbc.Connection;

public class DatabaseFactory {
	public static String url_;
	public static String username_;
	public static String password_;

	static Logger log = Logger.getLogger(DatabaseFactory.class);

	public static Connection New(String url, String username, String password) {
		url_ = url;
		username_ = username;
		password_ = password;
		return New();
	}

	public static Connection New() {
		if (url_ != null)
			try {
				return (Connection) DriverManager.getConnection(url_,
						username_, password_);
			} catch (SQLException e) {
				log.error("Initializing database failed", e);
			}
		return null;
	}
}
