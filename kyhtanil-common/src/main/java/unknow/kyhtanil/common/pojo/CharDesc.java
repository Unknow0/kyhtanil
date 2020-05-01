package unknow.kyhtanil.common.pojo;

/**
 * a char we can log into
 * 
 * @author unknow
 */
public class CharDesc {
	/** id of this car info */
	public int id;
	/** the name of the char */
	public String name;
	/** the level */
	public int level;

	/**
	 * create new CharDesc
	 */
	public CharDesc() {
	}

	/**
	 * create new CharDesc
	 * 
	 * @param id
	 * @param name
	 * @param level
	 */
	public CharDesc(int id, String name, int level) {
		this.id = id;
		this.name = name;
		this.level = level;
	}

	@Override
	public String toString() {
		return "CharDesc [id=" + id + ", name=" + name + ", level=" + level + "]";
	}
}
