package unknow.kyhtanil.server;

import java.sql.*;
import java.util.*;

import javax.naming.*;

import unknow.common.*;
import unknow.json.*;
import unknow.kyhtanil.common.component.*;
import unknow.kyhtanil.common.pojo.*;
import unknow.kyhtanil.server.component.*;
import unknow.kyhtanil.server.dao.Character;
import unknow.kyhtanil.server.pojo.*;
import unknow.kyhtanil.server.utils.*;
import unknow.orm.*;
import unknow.orm.criteria.*;
import unknow.orm.reflect.*;

public class Database
	{
	private unknow.orm.mapping.Database co;
	private Mappers mappers;

//	private BTree<Integer,WeaponStub> weapon=new BTree<Integer,WeaponStub>();

	public Database()
		{

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

	public void init(Mappers mappers) throws ClassNotFoundException, ClassCastException, InstantiationException, IllegalAccessException, ReflectException, JsonException, SQLException, NamingException
		{
		Mappings.load(null, Cfg.getSystem());
		co=Mappings.getDatabase("kyhtanil");
		this.mappers=mappers;
		}

	public boolean loginExist(String login) throws SQLException
		{
		Query query=co.createQuery("select id from accounts where lower(login)=lower(:login)");
		query.setString("login", login);
		QueryResult qr=query.execute();
		boolean r=qr.next();
		qr.close();
		query.close();
		return r;
		}

	public Account createAccount(String login, byte[] passHash)
		{
		Account a=new Account(login, passHash);
		try
			{
			co.insert(a);
			return a;
			}
		catch (SQLException e)
			{
			return null;
			}
		}

	public Account getAccount(String login, byte[] passHash) throws SQLException
		{
		Account a=null;
		Query query=co.createQuery("select {a} from accounts a where lower(login)=lower(:login)", new String[] {"a"}, new Class[] {Account.class});
		query.setString("login", login);
		QueryResult qr=query.execute();
		if(qr.next())
			{
			a=(Account)qr.getEntity("a");
			if(!Arrays.equals(passHash, a.getPassHash()))
				a=null;
			}
		qr.close();
		query.close();
		return a;
		}

	public boolean loadPj(int account, Integer id, int e) throws SQLException
		{
		Criteria crit=co.createCriteria(Character.class, "c");
		Join j=crit.addJoin(Body.class, "b");
		j.on(On.eq("body", "id"));
		j.setProjection(Projection.all());
		crit.add(Restriction.eq("id", id));
		crit.add(Restriction.eq("account", account));

		try (QueryResult qr=crit.execute())
			{
			if(qr.next())
				{
				ReflectFactory.entity.set(e);
				PositionComp p=mappers.position(e);
//			pj.x=5;
//			pj.y=5; // TODO
				p.x=p.y=5;

				Character c=qr.getEntity("c");
				qr.getEntity("b");

				MobInfoComp m=mappers.mobInfo(e);
				m.name=c.name;
				m.hp=c.hp;
				m.mp=c.mp;

//				m.level=b.level;
//				m.constitution=b.constitution;
//				m.strength=b.strength;
//				m.concentration=b.concentration;
//				m.intelligence=b.intelligence;
//				m.dexterity=b.dexterity;
				return true;
				}
			}
		return false;
		}

	public List<CharDesc> getCharList(int account) throws SQLException
		{
		List<CharDesc> list=new ArrayList<CharDesc>();
		try (Query query=co.createQuery("select id, name, level from characters where account=:account"))
			{
			query.setInt("account", account);
			try (QueryResult qr=query.execute())
				{
				while (qr.next())
					list.add(new CharDesc(qr.getInt("id"), qr.getString("name"), qr.getInt("level")));
				}
			}
		return list;
		}
	}