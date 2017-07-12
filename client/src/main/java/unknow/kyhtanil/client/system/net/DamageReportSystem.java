package unknow.kyhtanil.client.system.net;

import org.slf4j.*;

import unknow.kyhtanil.client.artemis.*;
import unknow.kyhtanil.common.component.*;
import unknow.kyhtanil.common.component.net.*;

import com.artemis.*;
import com.artemis.systems.*;

public class DamageReportSystem extends IteratingSystem
	{
	private static final Logger log=LoggerFactory.getLogger(DamageReportSystem.class);
	private ComponentMapper<DamageReport> report;
	private ComponentMapper<BooleanComp> done;
	private ComponentMapper<CalculatedComp> calculated;
	private UUIDManager manager;

	public DamageReportSystem()
		{
		super(Aspect.all(DamageReport.class, BooleanComp.class));
		}

	protected void process(int entityId)
		{
		DamageReport r=report.get(entityId);
		BooleanComp b=done.get(entityId);
		if(!b.value) // entity not finished to be created
			return;
		world.delete(entityId);
		log.info("{}", r);

		Integer e=manager.getEntity(r.uuid);
		if(e!=null)
			{
			CalculatedComp c=calculated.get(e);
			c.hp-=r.damage;
			}
		}
	}
