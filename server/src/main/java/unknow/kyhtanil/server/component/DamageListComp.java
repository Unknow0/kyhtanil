package unknow.kyhtanil.server.component;

public class DamageListComp extends CompositeComponent<DamageListComp.Damage> {
	public static class Damage {
		public int base;
		public int slashing;
		public int blunt;
		public int piercing;
		public int lightning;
		public int fire;
		public int ice;

		public float duration;

		public int source;

		public Damage(int source, int base, int slashing, int blunt, int piercing, int lightning, int fire, int ice, float duration) {
			this.source = source;
			this.base = base;
			this.slashing = slashing;
			this.blunt = blunt;
			this.piercing = piercing;
			this.lightning = lightning;
			this.fire = fire;
			this.ice = ice;
			this.duration = duration;
		}
	}
}
