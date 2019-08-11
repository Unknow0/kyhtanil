package unknow.kyhtanil.server;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptException;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.esotericsoftware.kryo.util.IntMap;

import unknow.common.Cfg;
import unknow.kyhtanil.common.component.Body;
import unknow.kyhtanil.common.component.MobInfoComp;
import unknow.kyhtanil.common.component.PositionComp;
import unknow.kyhtanil.common.pojo.CharDesc;
import unknow.kyhtanil.server.dao.Character;
import unknow.kyhtanil.server.pojo.Account;
import unknow.orm.Mappings;
import unknow.orm.Query;
import unknow.orm.QueryResult;
import unknow.orm.criteria.Criteria;
import unknow.orm.criteria.Join;
import unknow.orm.criteria.On;
import unknow.orm.criteria.Projection;
import unknow.orm.criteria.Restriction;

public class Database extends BaseSystem
	{
	private unknow.orm.mapping.Database co;

	private ComponentMapper<PositionComp> position;
	private ComponentMapper<MobInfoComp> mobInfo;

//	private BTree<Integer,WeaponStub> weapon=new BTree<Integer,WeaponStub>();

	public Database()
		{
		}

	@Override
	protected void processSystem()
		{
		}

	@Override
	public void initialize()
		{
		if(co!=null)
			return;
		try
			{
			ReflectFactory.world=world;
			Mappings.load(null, Cfg.getSystem());
			co=Mappings.getDatabase("kyhtanil");
			}
		catch (Exception e)
			{
			e.printStackTrace();
			System.exit(1);
			}
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
				PositionComp p=position.get(e);
//			pj.x=5;
//			pj.y=5; // TODO
				p.x=p.y=5;

				Character c=qr.getEntity("c");
				qr.getEntity("b");

				MobInfoComp m=mobInfo.get(e);
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
		try (Query query=co.createQuery("select c.id, name, level from characters c inner join characters_body b on c.body=b.id  where account=:account"))
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

	public IntMap<CompiledScript> processSkill(Compilable jsComp) throws SQLException, ScriptException
		{
		IntMap<CompiledScript> ret=new IntMap<>();
		try (Query query=co.createQuery("select id, code from skills"))
			{
			try (QueryResult qr=query.execute())
				{
				while (qr.next())
					ret.put(qr.getInt("id"), jsComp.compile(qr.getString("code")));
				}
			}
		return ret;
		}
	}