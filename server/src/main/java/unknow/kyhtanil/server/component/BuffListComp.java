package unknow.kyhtanil.server.component;

import unknow.kyhtanil.server.pojo.*;

public class BuffListComp extends CompositeComponent<BuffListComp.Buff>
	{
	// TODO cache

	public static class Buff
		{
		public float duration;
		public EffectType type;
		public int value;
		}
	}
