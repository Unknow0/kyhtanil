/*******************************************************************************
 * Copyright (c) 2014 Unknow.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.html
 * 
 * Contributors:
 * Unknow - initial API and implementation
 ******************************************************************************/
package unknow.kyhtanil.server.pojo;

import unknow.kyhtanil.common.pojo.*;

/** 
 * @author  Unknow
 */
public interface MobInterface
	{
	public void looseHp(int dmg);

	public void looseMp(int cost);

	public String name();

	public int hp();

	public int maxHp();

	public int mp();

	public int maxMp();

	public int strength();

	public int constitution();

	public int intelligence();

	public int concentration();

	public int dexterity();

	public int hitChance();

	public int evade();

	public int spellFocus();

	public int spellResist();

	public int strengthTotal();

	public int constitutionTotal();

	public int intelligenceTotal();

	public int concentrationTotal();

	public int dexterityTotal();

	public int hitChanceTotal();

	public int evadeTotal();

	public int spellFocusTotal();

	public int spellResistTotal();

	public int carryWeigth();

	public Resistance resistance();

	public Damage damage();

	public int lvl();

	public int gold();

	public double speed();

	public double weaponSpeed();

	public double rawSpeed();

	public void addBuff(Buff buff);

	public void removeBuff(Buff buff);

	public boolean equip(Item i);

	public void unequip(Item.Slot slot);

	public Item getEquip(Item.Slot slot);

	public void addPassive(SkillInterface skill, Effect e);

	/** retourne le niveau du skill. */
	public int skillLevel(SkillInterface s);

	/** retourne le niveau d'une dicipline. */
	public int disciplineLevel(Discipline d);
	}
