package unknow.kyhtanil.common;

import unknow.kyhtanil.common.pojo.*;

public abstract class Skill {
	public int id;

	public String name;

	public Skill(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public abstract int cost();

	public abstract void exec(int self, Point p, Integer t);

	public abstract String desc();
}
