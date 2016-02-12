package unknow.kyhtanil.server;

import java.sql.*;
import java.util.*;

import javax.naming.*;

import unknow.common.*;
import unknow.json.*;
import unknow.kyhtanil.common.*;
import unknow.kyhtanil.server.component.*;
import unknow.kyhtanil.server.pojo.*;
import unknow.kyhtanil.server.utils.*;
import unknow.orm.*;
import unknow.orm.reflect.*;

public class Database
	{
	private unknow.orm.mapping.Database co;
	private Mappers mappers;

//	private BTree<Integer,WeaponStub> weapon=new BTree<Integer,WeaponStub>();

	public Database() throws ClassNotFoundException, ClassCastException, InstantiationException, IllegalAccessException, JsonException, SQLException, NamingException, ReflectException
		{
		Mappings.load(null, Cfg.getSystem());
		co=Mappings.getDatabase("game");

//		Criteria crit=co.createCriteria(WeaponStub.class, "w");
//		try (QueryResult r=crit.execute())
//			{
//			while (r.next())
//				{
//				WeaponStub w=r.getEntity("r");
//				weapon.put(w.id(), w);
//				}
//			}
		}

	public void setMappers(Mappers mappers)
		{
		this.mappers=mappers;
		}

	public boolean loginExist(String login) throws SQLException
		{
		Query query=co.createQuery("select id from accounts where login=:login");
		query.setString("login", login);
		QueryResult qr=query.execute();
		boolean r=qr.next();
		qr.close();
		query.close();
		return r;
		}

	public Account getAccount(String login, String pass) throws SQLException
		{
		Account a=null;
		Query query=co.createQuery("select {a} from accounts a where login=:login", new String[] {"a"}, new Class[] {Account.class});
		query.setString("login", login);
		QueryResult qr=query.execute();
		if(qr.next())
			{
			a=(Account)qr.getEntity("a");
			if(!pass.equals(a.getPass()))
				a=null;
			}
		qr.close();
		query.close();
		return a;
		}

	public boolean loadPj(int account, Integer id, int e) throws SQLException
		{
		try (Query query=co.createQuery("select {c} from characters c where id=:id and account=:account", new String[] {"c"}, new Class[] {Pj.class}))
			{
			query.setInt("id", id);
			query.setInt("account", account);
			try (QueryResult qr=query.execute())
				{
				if(qr.next())
					{
					PositionComp p=mappers.position(e);
//			pj.x=5;
//			pj.y=5; // TODO
					p.x=p.y=5;

					MobInfoComp m=mappers.mobInfo(e);
					m.name=qr.getString("c.name");
					m.level=qr.getInt("c.level");
					m.hp=qr.getInt("c.hp");
					m.mp=qr.getInt("c.mp");
					m.constitution=qr.getInt("c.constitution");
					m.strength=qr.getInt("c.strength");
					m.concentration=qr.getInt("c.concentration");
					m.intelligence=qr.getInt("c.intelligence");
					m.dexterity=qr.getInt("c.dexterity");
					return true;
					}
				}
			}
		return false;
		}

	public List<CharDesc> getCharList(int account) throws SQLException
		{
		Query query=co.createQuery("select id, name, level from characters where account=:account");
		query.setInt("account", account);
		QueryResult qr=query.execute();
		List<CharDesc> list=new ArrayList<CharDesc>();
		while (qr.next())
			{
			list.add(new CharDesc(qr.getInt("id"), qr.getString("name"), qr.getInt("level")));
			}
		qr.close();
		query.close();
		return list;
		}
	}