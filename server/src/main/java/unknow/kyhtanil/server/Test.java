package unknow.kyhtanil.server;

import unknow.common.*;
import unknow.kyhtanil.server.pojo.*;
import unknow.orm.*;
import unknow.orm.annotation.*;
import unknow.orm.criteria.*;
import unknow.orm.mapping.Database;

public class Test
	{
//	@Validate
	public static void main(String[] arg) throws Exception
		{
		Mappings.load("", Cfg.getSystem());

		Database database=Mappings.getDatabase("game");

		@Validate
		Criteria crit=database.createCriteria(Account.class, "a");
		crit.add(Restriction.eq("login", "Test"));
		crit.setProjection(Projection.property("id"));

		Join join=crit.addJoin(Pj.class, "p");
		join.on(On.eq("a.id", "p.account"));

		System.out.println(crit.getSql());
		QueryResult rs=crit.execute();
		while (rs.next())
			{
			Object entity=rs.getEntity("a");
			}
		}
	}
