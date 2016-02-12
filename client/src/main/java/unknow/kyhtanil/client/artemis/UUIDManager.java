package unknow.kyhtanil.client.artemis;

import unknow.kyhtanil.common.util.*;

import com.artemis.*;

public class UUIDManager extends BaseUUIDManager
	{
	public UUIDManager()
		{
		super(Aspect.all());
		}

	@Override
	protected void initialize()
		{
		}

	@Override
	public void removed(int entityId)
		{
		remove(entityId);
		}
	}
