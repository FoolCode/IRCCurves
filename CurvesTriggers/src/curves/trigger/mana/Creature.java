package curves.trigger.mana;

import java.sql.ResultSet;

import org.apache.log4j.Logger;

import com.mysql.jdbc.PreparedStatement;

import curves.main.Bot;
import curves.main.Profile;

public class Creature {

	Logger log = Logger.getLogger(Creature.class);
	
	static int FIRE = 1 << 0;
	static int WATER = 1 << 1;
	static int AIR = 1 << 2;
	static int EARTH = 1 << 3;
	static int SACRIFICE = 1 << 4;
	static int TERRORIST = 1 << 5;
	static int LEECHMANA = 1 << 6;

	private Profile user;
	private Bot bot;
	private int id = 0;
	private int cost = 0;
	private int atk = 0;
	private int def = 0;
	private int levelup = 0;
	private float levelup_chance = 0;
	private int attribute = 0;
	private String name = "";
	
	private boolean died = false;
	
	public boolean died(){
		return died;
	}

	public int getAtk() {
		return atk;
	}

	public int getDef() {
		return def;
	}

	public int getAttribute() {
		return attribute;
	}

	public String getName() {
		return name;
	}

	public int getCost() {
		return cost;
	}

	public boolean exists() {
		return id > 0;
	}

	public String toString() {
		return getName();
	}

	public Creature(Profile user, Bot bot) {
		this.bot = bot;
		this.user = user;
		try {
			PreparedStatement ps = (PreparedStatement) bot
					.getDB()
					.prepareStatement(
							"SELECT mana_creature.* FROM mana_creature, mana WHERE mana.creature_id = mana_creature.creature_id"
									+ " AND mana.user = ?;");
			ps.setString(1, user.getNickname());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				id = rs.getInt("creature_id");
				name = rs.getString("name");
				atk = rs.getInt("atk");
				def = rs.getInt("def");
				levelup = rs.getInt("levelup_id");
				levelup_chance = rs.getFloat("levelup_chance");
				attribute = rs.getInt("attribute");
				cost = rs.getInt("cost");
			}
			ps.close();
		} catch (Exception e) {
			log.error("Unable to fetch creature from database.", e);
		}
	}

	public Creature(Profile user, int payment, Bot bot) {
		this.bot = bot;
		this.user = user;
		try {
			PreparedStatement ps = (PreparedStatement) bot
					.getDB()
					.prepareStatement(
							"SELECT * FROM mana_creature WHERE cost <= ? AND creature_id > 0 ORDER BY rand();");
			ps.setInt(1, payment);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				id = rs.getInt("creature_id");
				name = rs.getString("name");
				atk = rs.getInt("atk");
				def = rs.getInt("def");
				levelup = rs.getInt("levelup_id");
				levelup_chance = rs.getFloat("levelup_chance");
				attribute = rs.getInt("attribute");
				cost = rs.getInt("cost");
				if (Math.random() < rs.getFloat("chance"))
					break;
			}
			ps.close();
			if (this.exists()) {
				ps = (PreparedStatement) bot
						.getDB()
						.prepareStatement(
								"UPDATE mana SET mana = mana - ?, creature_id = ? WHERE user = ?;");
				ps.setInt(1, cost);
				ps.setInt(2, id);
				ps.setString(3, user.getNickname());
				ps.executeUpdate();
				ps.close();
			}
		} catch (Exception e) {
			log.error("Unable to summon new creature.", e);
		}

	}

	public void dies(){
		try {
			PreparedStatement ps = (PreparedStatement) bot
					.getDB()
					.prepareStatement(
							"UPDATE mana SET creature_id = 0 WHERE user = ?;");
			ps.setString(1, user.getNickname());
			ps.executeUpdate();
			ps.close();
			id = 0;
		} catch (Exception e) {
			log.error("Unable to kill creature in the database.", e);
		}
		died = true;
	}
	
	public boolean levelup(int killedCost) {
		if (died) return false;
		if (Math.random()/(Math.sqrt(killedCost/4+1)) < levelup_chance) {
			try {
				PreparedStatement ps = (PreparedStatement) bot
						.getDB()
						.prepareStatement(
								"SELECT * FROM mana_creature WHERE creature_id = ?;");
				ps.setInt(1, levelup);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					id = rs.getInt("creature_id");
					name = rs.getString("name");
					atk = rs.getInt("atk");
					def = rs.getInt("def");
					levelup = rs.getInt("levelup_id");
					levelup_chance = rs.getFloat("levelup_chance");
					attribute = rs.getInt("attribute");
					cost = rs.getInt("cost");
					if (Math.random() < rs.getFloat("chance"))
						break;
				}
				ps.close();
				ps = (PreparedStatement) bot
						.getDB()
						.prepareStatement(
								"UPDATE mana SET creature_id = ? WHERE user = ?;");
				ps.setInt(1, id);
				ps.setString(2, user.getNickname());
				ps.executeUpdate();
				ps.close();
			} catch (Exception e) {
				log.error("Unable to level up the creature.", e);
			}
			return true;
		} else {
			return false;
		}
	}
}
