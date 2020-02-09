package unknow.kyhtanil.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import unknow.kyhtanil.common.component.PositionComp;
import unknow.kyhtanil.common.component.StatBase;
import unknow.kyhtanil.common.component.StatShared;
import unknow.kyhtanil.common.pojo.CharDesc;

public class Database extends BaseSystem {
	private DataSource ds;

	private ComponentMapper<PositionComp> position;
	private ComponentMapper<StatShared> statShared;
	private ComponentMapper<StatBase> statBase;

	public Database() {
	}

	@Override
	protected void processSystem() {
	}

	@Override
	public void initialize() {
		ds = new HikariDataSource(new HikariConfig("/hikari.properties"));
	}

	private static interface STInit {
		public void init(PreparedStatement st) throws SQLException;
	}

	private static interface RSConvert<T> {
		public T convert(ResultSet rs) throws SQLException;
	}

	private boolean sqlrun(String sql, STInit init) throws SQLException {
		try (Connection c = ds.getConnection()) {
			PreparedStatement st = c.prepareStatement(sql);
			init.init(st);
			try (ResultSet rs = st.executeQuery()) {
				return rs.next();
			}
		}
	}

	private <T> T sqlrun(String sql, STInit stinit, RSConvert<T> convert) throws SQLException {
		try (Connection c = ds.getConnection()) {
			PreparedStatement st = c.prepareStatement(sql);
			stinit.init(st);
			try (ResultSet rs = st.executeQuery()) {
				if (!rs.next())
					return null;
				return convert.convert(rs);
			}
		}
	}

	private <T> List<T> sqlrunall(String sql, STInit stinit, RSConvert<T> convert) throws SQLException {
		try (Connection c = ds.getConnection()) {
			PreparedStatement st = c.prepareStatement(sql);
			stinit.init(st);
			try (ResultSet rs = st.executeQuery()) {
				List<T> list = new ArrayList<>();
				while (rs.next())
					list.add(convert.convert(rs));
				return list;
			}
		}
	}

	private int sqlupdate(String sql, STInit init) throws SQLException {
		try (Connection c = ds.getConnection()) {
			PreparedStatement st = c.prepareStatement(sql);
			init.init(st);
			return st.executeUpdate();
		}
	}

	private <T> T sqlinsert(String sql, STInit init, RSConvert<T> conv) throws SQLException {
		try (Connection c = ds.getConnection()) {
			PreparedStatement st = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			init.init(st);
			st.executeUpdate();
			ResultSet rs = st.getGeneratedKeys();
			return conv.convert(rs);
		}
	}

	public boolean loginExist(String login) throws SQLException {
		return sqlrun("select id from accounts where lower(login)=lower(?)", st -> st.setString(1, login));
	}

	public Integer createAccount(String login, byte[] passHash) throws SQLException {
		return sqlinsert("insert into account (login, pass_hash) values (?,?)", st -> {
			st.setString(1, login);
			st.setBytes(2, passHash);
		}, rs -> rs.getInt(0));
	}

	public Integer getAccount(String login, byte[] passHash) throws SQLException {
		return sqlrun("select * from accounts where lower(login)=lower(?) and pass_hash=?", st -> {
			st.setString(1, login);
			st.setBytes(2, passHash);
		}, rs -> rs.getInt("id"));
	}

	public boolean loadPj(int account, Integer id, int e) throws SQLException {
		return null != sqlrun("select" // @formatter:off
				+ " c.name, c.hp, c.mp, b.strength, b.constitution, b.intelligence, b.concentration, b.dexterity, b.points, b.xp, b.level"
				+ " from characters c"
				+ " inner join characters_body b on b.id=c.body"
				+ " where c.account=? and c.id=?"// @formatter:on
				, st -> {
					st.setInt(1, account);
					st.setInt(2, id);
				}, rs -> {
					PositionComp p = position.get(e);
					// pj.x=5;
					// pj.y=5; // TODO
					p.x = p.y = 5;

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

					return rs;
				});
	}

	public List<CharDesc> getCharList(int account) throws SQLException {
		return sqlrunall("select c.id, name, level from characters c inner join characters_body b on c.body=b.id  where account=?", st -> st.setInt(1, account), rs -> new CharDesc(rs.getInt("id"), rs.getString("name"), rs.getInt("level")));
	}

	public void createChar(int account, String name) throws SQLException {
		sqlinsert("insert into characters_body default values returning id;" + 
				"insert into characters (name, account, body) values (?, ?, lastval());", st->{st.setString(1, name); st.setInt(2, account);}, rs->rs.getInt(0));
	}
}