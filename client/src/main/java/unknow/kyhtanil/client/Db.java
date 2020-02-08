package unknow.kyhtanil.client;

import java.sql.*;
import java.util.*;

import unknow.common.*;
import unknow.kyhtanil.client.dao.*;
import unknow.orm.*;
import unknow.orm.criteria.*;
import unknow.orm.mapping.*;

public class Db {
	private static final Database data;
	static {
		try {
			Mappings.load("", Cfg.getSystem());
			data = Mappings.getDatabase("db");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static MapDao getMap(int id) throws SQLException {
		Criteria crit = data.createCriteria(MapDao.class, "a");
		crit.add(Restriction.eq("id", id));
		try (QueryResult rs = crit.execute()) {
			if (rs.next())
				return rs.getEntity("a");
			return null;
		}
	}

	public static List<MapDao> getMaps() throws SQLException {
		Criteria crit = data.createCriteria(MapDao.class, "a");
		List<MapDao> list = new ArrayList<MapDao>();
		try (QueryResult rs = crit.execute()) {
			while (rs.next())
				list.add((MapDao) rs.getEntity("a"));
		}
		return list;
	}

	public static void insertMap(String map, String tileset) throws SQLException {
		Integer id = null;
		try (Query query = data.createQuery("select id from maps where name=?")) {
			query.setString(1, map);
			try (QueryResult rs = query.execute()) {
				if (rs.next())
					id = rs.getInt("id");
			}
		}

		if (id == null) {
			try (Query query = data.createQuery("insert into maps (name, tileset) values (?, ?)")) {
				query.setString(1, map);
				query.setString(2, tileset);
				query.executeUpdate();
			}
		} else {
			try (Query query = data.createQuery("update maps set tileset=? where id=?")) {
				query.setInt(2, id);
				query.setString(1, tileset);
				query.executeUpdate();
			}
		}
	}

	public static String getMobTex(Integer type) throws SQLException {
		Criteria crit = data.createCriteria(MobDao.class);
		crit.setProjection(Projection.property("texture"));
		crit.add(Restriction.eq("id", type));
		try (QueryResult rs = crit.execute()) {
			if (rs.next())
				return rs.getString("texture");
		}
		return null;
	}
}
