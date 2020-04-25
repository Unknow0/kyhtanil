package unknow.kyhtanil.common;

public enum Stats {
	/** base stat */
	STAT_STRENGTH, STAT_CONSTITUTION, STAT_INTELLIGENCE, STAT_CONCENTRATION, STAT_DEXTERITY,
	/** computed */
	HP, HP_MAX, MP, MP_MAX, MOVE_SPEED, HP_REGEN, MP_REGEN,
	/** weapon damage */
	WPN_DMG_SLASH, WPN_DMG_PIERCE, WPN_DMG_BLUNT, WPN_DMG_FIRE, WPN_DMG_ICE, WPN_DMG_LIGTHNING;

	public static final Stats[] BASE = new Stats[] { STAT_STRENGTH, STAT_CONSTITUTION, STAT_INTELLIGENCE, STAT_CONCENTRATION, STAT_DEXTERITY };

	public static int baseHp(int constitution) {
		return constitution * 15 + 10;
	}

	public static int baseMp(int intelligence) {
		return intelligence * 9 + 10;
	}
}
