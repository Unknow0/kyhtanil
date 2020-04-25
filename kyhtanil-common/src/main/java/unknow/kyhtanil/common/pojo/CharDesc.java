package unknow.kyhtanil.common.pojo;

public class CharDesc {
	public int id;
	public String name;
	public int level;

	public CharDesc() {
	}

	public CharDesc(java.lang.Integer id, java.lang.String name, java.lang.Integer level) {
		this.id = id;
		this.name = name;
		this.level = level;
	}

	@Override
	public String toString() {
		return "CharDesc [id=" + id + ", name=" + name + ", level=" + level + "]";
	}
}
