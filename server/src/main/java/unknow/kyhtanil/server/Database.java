package unknow.kyhtanil.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import unknow.common.data.IntMap;
import unknow.kyhtanil.common.Stats;
import unknow.kyhtanil.common.component.Position;
import unknow.kyhtanil.common.component.StatBase;
import unknow.kyhtanil.common.component.StatShared;
import unknow.kyhtanil.common.pojo.CharDesc;
import unknow.kyhtanil.server.component.Archetypes;
import unknow.kyhtanil.server.component.Spawner;
import unknow.kyhtanil.server.pojo.IdRate;
import unknow.kyhtanil.server.pojo.ItemTemplate;
import unknow.kyhtanil.server.pojo.ItemTemplate.StatTemplate;
import unknow.kyhtanil.server.pojo.Mob;

/**
 * database connection
 * 
 * @author unknow
 */
public class Database extends BaseSystem {
	private static final IdRate[] IDRATES = new IdRate[0];
	private static final StatTemplate[] STAT_TEMPLATE = new StatTemplate[0];
	private static final RSConvert<Integer> FIRT_INT = rs -> rs.getInt(1);

	private DataSource ds;

	private Archetypes archetype;
	private ComponentMapper<Position> position;
	private ComponentMapper<StatShared> statShared;
	private ComponentMapper<StatBase> statBase;
	private ComponentMapper<Spawner> spawner;

	/** cache */
	private final IntMap<Mob> mobs = new IntMap<>();
	/** cache */
	private final IntMap<ItemTemplate> items = new IntMap<>();

	/**
	 * create new Database
	 */
	public Database() {
	}

	@Override
	protected void processSystem() {
	}

	@Override
	public void initialize() {
		ds = new HikariDataSource(new HikariConfig("/hikari.properties"));
	}

	/**
	 * all lazy load
	 * 
	 * @throws SQLException
	 */
	public void init() throws SQLException {
		List<Object> list = new ArrayList<>();
		class Convert implements RSConvert<IdRate[]> {
			private String c;
			private boolean standard;

			public Convert(String c) {
				this(c, false);
			}

			public Convert(String c, boolean standard) {
				this.c = c;
				this.standard = standard;
			}

			@Override
			public IdRate[] convert(ResultSet rs) throws SQLException {
				list.clear();
				float sum = 0;
				while (rs.next()) {
					IdRate r = new IdRate();
					r.id = rs.getInt(c);
					r.rate = rs.getFloat(standard ? "factor" : "rate");
					sum += r.rate;
					list.add(r);
				}
				if (standard) {
					for (Object o : list) {
						IdRate r = (IdRate) o;
						r.rate = r.rate / sum;
					}
				}
				return list.toArray(IDRATES);
			}
		}
		// load mobs
		exec("select * from mobs", rs -> {
			while (rs.next()) {
				int id = rs.getInt("id");
				Mob mob = new Mob(rs);
				mob.loots = query("select * from mobs_loot where mob=?", new Convert("item"), id);
				mobs.put(id, mob);
			}
		});

		// load items
		exec("select * from items", rs -> {
			while (rs.next()) {
				int item = rs.getInt("id");
				list.clear();
				exec("select * from items_stats where item=?", rss -> {
					while (rss.next())
						list.add(new StatTemplate(rss));
				}, item);
				items.put(item, new ItemTemplate(item, list.toArray(STAT_TEMPLATE)));
			}
		});

		// load spawners
		exec("select * from spawners", rs -> {
			while (rs.next()) {
				int i = world.create(archetype.spawner);
				Spawner s = spawner.get(i);
				s.x = rs.getFloat("x");
				s.y = rs.getFloat("y");
				s.r = rs.getFloat("r");
				s.max = rs.getInt("max");
				s.speed = rs.getInt("speed");

				s.mobs = query("select * from spawner_mobs where spawner=?", new Convert("mob", true), rs.getInt("id"));
			}
		});
	}

	/**
	 * @param id the mob id
	 * @return the mob
	 */
	public Mob getMob(int id) {
		return mobs.get(id);
	}

	public ItemTemplate getItem(int id) {
		return items.get(id);
	}

	private static interface RSConvert<T> {
		public T convert(ResultSet rs) throws SQLException;
	}

	private static interface RSConsumer {
		public void accept(ResultSet rs) throws SQLException;
	}

	private <T> T query(String sql, RSConvert<T> convert, Object... param) throws SQLException {
		try (Connection c = ds.getConnection()) {
			PreparedStatement st = c.prepareStatement(sql);
			for (int i = 0; i < param.length; i++)
				st.setObject(i + 1, param[i]);
			try (ResultSet rs = st.executeQuery()) {
				return convert.convert(rs);
			}
		}
	}

	private void exec(String sql, RSConsumer consumer, Object... param) throws SQLException {
		try (Connection c = ds.getConnection()) {
			PreparedStatement st = c.prepareStatement(sql);
			for (int i = 0; i < param.length; i++)
				st.setObject(i + 1, param[i]);
			try (ResultSet rs = st.executeQuery()) {
				consumer.accept(rs);
			}
		}
	}

	private int sqlupdate(String sql, Object... param) throws SQLException {
		try (Connection c = ds.getConnection()) {
			PreparedStatement st = c.prepareStatement(sql);
			for (int i = 0; i < param.length; i++)
				st.setObject(i + 1, param[i]);
			return st.executeUpdate();
		}
	}

	private <T> T sqlinsert(String sql, RSConvert<T> conv, Object... param) throws SQLException {
		try (Connection c = ds.getConnection()) {
			PreparedStatement st = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			for (int i = 0; i < param.length; i++)
				st.setObject(i + 1, param[i]);
			st.executeUpdate();
			ResultSet rs = st.getGeneratedKeys();
			if (!rs.next())
				return null;
			return conv.convert(rs);
		}
	}

	/**
	 * create a new account
	 * 
	 * @param login    the login
	 * @param passHash the hashed pass
	 * @return the account id or null if login already exists
	 * @throws SQLException
	 */
	public Integer createAccount(String login, byte[] passHash) throws SQLException {
		return sqlinsert("insert into accounts (login, pass_hash) values (?,?)", FIRT_INT, login, passHash);
	}

	/**
	 * get the account id
	 * 
	 * @param login    the login
	 * @param passHash the hashed pass
	 * @return the account id or null if the login or pass invalid
	 * @throws SQLException
	 */
	public Integer getAccount(String login, byte[] passHash) throws SQLException {
		return query("select id from accounts where lower(login)=lower(?) and pass_hash=?", rs -> rs.next() ? rs.getInt("id") : null, login, passHash);
	}

	/**
	 * list all char for an account
	 * 
	 * @param account the account
	 * @return the list of charDesc
	 * @throws SQLException
	 */
	public List<CharDesc> getCharList(int account) throws SQLException {
		return query("select c.id, name, level from characters c inner join characters_body b on c.body=b.id  where account=?", rs -> {
			List<CharDesc> list = new ArrayList<>();
			while (rs.next())
				list.add(new CharDesc(rs.getInt("id"), rs.getString("name"), rs.getInt("level")));
			return list;
		}, account);
	}

	/**
	 * load a character
	 * 
	 * @param account the account
	 * @param id      the char id
	 * @param e       the entity to load char onto
	 * @return true if the char is well loaded
	 * @throws SQLException
	 */
	public boolean loadPj(int account, Integer id, int e) throws SQLException {
		return query(
				"select" // @formatter:off
				+ " c.name, c.hp, c.mp, b.strength, b.constitution, b.intelligence, b.concentration, b.dexterity, b.points"
				+ " from characters c"
				+ " inner join characters_body b on b.id=c.body"
				+ " where c.account=? and c.id=?"// @formatter:on
				, rs -> {
					if (!rs.next())
						return false;
					Position p = position.get(e);
					p.x = p.y = 60; // TODO

					StatShared m = statShared.get(e);
					m.name = rs.getString("name");
					m.hp = rs.getInt("hp");
					m.mp = rs.getInt("mp");

					StatBase b = statBase.get(e);
					b.constitution = rs.getInt("constitution");
					b.strength = rs.getInt("strength");
					b.concentration = rs.getInt("concentration");
					b.intelligence = rs.getInt("intelligence");
					b.dexterity = rs.getInt("dexterity");
					b.xp = 50;
					b.level = 0;

					// TODO load inventory

					return true;
				}, account, id);
	}

	/**
	 * create a character
	 * 
	 * @param account the account id
	 * @param name    the name of the new char
	 * @throws SQLException
	 */
	public void createChar(int account, String name) throws SQLException {
		sqlinsert("insert into characters (name,account,hp,mp) values (?,?,?,?)", rs -> {
			int c = rs.getInt(1);
			int b = sqlinsert("insert into characters_body default values", FIRT_INT);
			sqlupdate("update characters set body=? where id=?", b, c);
			return null;
		}, name, account, Stats.baseHp(0), Stats.baseMp(0));
	}

}