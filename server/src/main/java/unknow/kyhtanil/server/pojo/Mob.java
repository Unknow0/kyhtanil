/**
 * 
 */
package unknow.kyhtanil.server.pojo;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author unknow
 */
public class Mob {
	/** name of the mob */
	public String name;
	/** texture to use */
	public String tex;
	/** width of the texture */
	public float w;

	/** base stats */
	public int strength;
	/** base stats */
	public int constitution;
	/** base stats */
	public int intelligence;
	/** base stats */
	public int concentration;
	/** base stats */
	public int dexterity;

	public IdRate[] loots;

	/**
	 * create new Mob
	 * 
	 * @param rs result set to read
	 * @throws SQLException
	 */
	public Mob(ResultSet rs) throws SQLException {
		name = rs.getString("name");
		tex = rs.getString("tex");
		w = rs.getFloat("w");

		strength = rs.getInt("strength");
		constitution = rs.getInt("constitution");
		intelligence = rs.getInt("intelligence");
		concentration = rs.getInt("concentration");
		dexterity = rs.getInt("dexterity");
	}
}
