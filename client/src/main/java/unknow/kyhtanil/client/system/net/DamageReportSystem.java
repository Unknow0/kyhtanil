package unknow.kyhtanil.client.system.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

import unknow.kyhtanil.common.component.StatShared;
import unknow.kyhtanil.common.component.net.DamageReport;
import unknow.kyhtanil.common.util.BaseUUIDManager;

public class DamageReportSystem extends IteratingSystem {
	private static final Logger log = LoggerFactory.getLogger(DamageReportSystem.class);
	private ComponentMapper<DamageReport> report;
	private ComponentMapper<StatShared> mob;
	private BaseUUIDManager manager;

	public DamageReportSystem() {
		super(Aspect.all(DamageReport.class));
	}

	protected void process(int entityId) {
		DamageReport r = report.get(entityId);
		world.delete(entityId);
		log.info("{}", r);

		Integer e = manager.getEntity(r.uuid);
		if (e != null) {
			StatShared c = mob.get(e);
			c.hp -= r.damage;
		}
	}
}
