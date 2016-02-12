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

import java.util.*;

/**
 * 
 * @author Unknow
 */
public interface PjInterface extends MobInterface
	{
	public long xp();

	public boolean looseGold(int nbr);

	public boolean addGold(int nbr);

	public void restoreAll();

	public boolean upStrength(int val);

	public boolean upConstitution(int val);

	public boolean upIntelligence(int val);

	public boolean upConcentration(int val);

	public boolean upDexterity(int val);

	/** @return true if levelup */
	public boolean addXp(int amount);

	public boolean addItem(Item i);

	public int inventorySize();

	public Item getItem(int i);

	public int skillPoints();

	public int statPoints();

	public boolean removeItem(int i);

	/** recupere la liste des skill appris par se personnage. */
	public Set<SkillInterface> skills();

	public boolean upSkill(SkillInterface s);

	public boolean upDiscipline(Discipline d);
	}
