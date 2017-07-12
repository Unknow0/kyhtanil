package unknow.kyhtanil.common.component;

import com.artemis.*;

public class BooleanComp extends PooledComponent
	{
	public boolean value=false;

	public BooleanComp()
		{
		}

	public BooleanComp(boolean value)
		{
		this.value=value;
		}

	public void reset()
		{
		value=false;
		}

	public String toString()
		{
		return "value: "+value;
		}
	}