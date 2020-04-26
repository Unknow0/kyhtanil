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

import unknow.kyhtanil.common.Stats;
import unknow.kyhtanil.common.component.Position;
import unknow.kyhtanil.common.component.StatBase;
import unknow.kyhtanil.common.component.StatShared;
import unknow.kyhtanil.common.pojo.CharDesc;
import unknow.kyhtanil.server.component.Archetypes;
import unknow.kyhtanil.server.component.SpawnerComp;

public class Database extends BaseSystem {
	private static final RSConvert<Boolean> HAS_NEXT = rs -> rs.next();
	private static final RSConvert<Integer> FIRT_INT = rs -> rs.getInt(1);

	private DataSource ds;

	private Archetypes archetype;
	private ComponentMapper<Position> position;
	private ComponentMapper<StatShared> statShared;
	private ComponentMapper<StatBase> statBase;
	private ComponentMapper<SpawnerComp> spawner;

	public Database() {
	}

	@Override
	protected void processSystem() {
	}

	@Override
	public void initialize() {
		ds = new HikariDataSource(new HikariConfig("/hikari.properties"));
	}

	private static interface RSConvert<T> {
		public T convert(ResultSet rs) throws SQLException;
	}

	private <T> T exec(String sql, RSConvert<T> convert, Object... param) throws SQLException {
		try (Connection c = ds.getConnection()) {
			PreparedStatement st = c.prepareStatement(sql);
			for (int i = 0; i < param.length; i++)
				st.setObject(i + 1, param[i]);
			try (ResultSet rs = st.executeQuery()) {
				return convert.convert(rs);
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

	public boolean loginExist(String login) throws SQLException {
		return exec("select id from accounts where lower(login)=lower(?)", HAS_NEXT, login);
	}

	public Integer createAccount(String login, byte[] passHash) throws SQLException {
		return sqlinsert("insert into accounts (login, pass_hash) values (?,?)", FIRT_INT, login, passHash);
	}

	public Integer getAccount(String login, byte[] passHash) throws SQLException {
		return exec("select * from accounts where lower(login)=lower(?) and pass_hash=?", rs -> rs.next() ? rs.getInt("id") : null, login, passHash);
	}

	public boolean loadPj(int account, Integer id, int e) throws SQLException {
		return exec(
				"select" // @formatter:off
				+ " c.name, c.hp, c.mp, b.strength, b.constitution, b.intelligence, b.concentration, b.dexterity, b.points"
				+ " from characters c"
				+ " inner join characters_body b on b.id=c.body"
				+ " where c.account=? and c.id=?"// @formatter:on
				, rs -> {
					if (!rs.next())
						return false;
					Position p = position.get(e);
					// pj.x=5;
					// pj.y=5; // TODO
					p.x = p.y = 60;

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

					// b.level = b.level;

					return true;
				}, account, id);
	}

	public List<CharDesc> getCharList(int account) throws SQLException {
		return exec("select c.id, name, level from characters c inner join characters_body b on c.body=b.id  where account=?", rs -> {
			List<CharDesc> list = new ArrayList<>();
			while (rs.next())
				list.add(new CharDesc(rs.getInt("id"), rs.getString("name"), rs.getInt("level")));
			return list;
		}, account);
	}

	public void createChar(int account, String name) throws SQLException {
		sqlinsert("insert into characters (name,account,hp,mp) values (?,?,?,?)", rs -> {
			int c = rs.getInt(1);
			int b = sqlinsert("insert into characters_body default values", FIRT_INT);
			sqlupdate("update characters set body=? where id=?", b, c);
			return null;
		}, name, account, Stats.baseHp(0), Stats.baseMp(0));
	}

	public void loadSpawner() throws SQLException {
		exec("select * from spawners", rs -> {
			while (rs.next()) {
				int i = world.create(archetype.spawner);
				SpawnerComp s = spawner.get(i);
				s.x = rs.getFloat("x");
				s.y = rs.getFloat("y");
				s.r = rs.getFloat("r");
				s.max = rs.getInt("max");
				s.speed = rs.getInt("speed");
			}
			return null;
		});
	}
}