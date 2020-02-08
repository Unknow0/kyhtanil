package unknow.kyhtanil.common;

public class Effect {
	public Stats stat;
	public float value;
	public Type type;
	public int ttl;

	public static enum Type {
		flat, add, more
	}
}
