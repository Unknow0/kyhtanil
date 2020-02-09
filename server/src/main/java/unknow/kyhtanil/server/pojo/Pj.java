///*******************************************************************************
// * Copyright (c) 2014 Unknow.
// * All rights reserved. This program and the accompanying materials
// * are made available under the terms of the GNU Lesser Public License v3
// * which accompanies this distribution, and is available at
// * http://www.gnu.org/licenses/lgpl-3.0.html
// * 
// * Contributors:
// * Unknow - initial API and implementation
// ******************************************************************************/
//package unknow.kyhtanil.server.pojo;
//
//import java.util.*;
//
//import unknow.kyhtanil.server.*;
//import unknow.kyhtanil.server.pojo.SkillInterface.*;
//
//public class Pj extends Mob implements PjInterface
//	{
//	protected int statPoints;
//	protected int skillPoints;
//	protected long xp;
//
//	protected Map<Discipline,Integer> disciplinesLevel=new HashMap<>();
//	protected Map<SkillInterface,Integer> skillsLevel=new HashMap<>();
//
//	protected List<Item> inventory=new ArrayList<>();
//
//	public Pj()
//		{
//		}
//
//	public Pj(String name)
//		{
//		super(name, 1, 1, 1, 1, 1, 1);
//		skillPoints=3;
//		statPoints=35;
//		}
//
//	public long xp()
//		{
//		return xp;
//		}
//
//	public boolean looseGold(int nbr)
//		{
//		if(gold<nbr)
//			return false;
//		gold-=nbr;
//		return true;
//		}
//
//	public boolean addGold(int nbr)
//		{
//		gold+=nbr;
//		return true;
//		}
//
//	public void restoreAll()
//		{
//		hp=hpMax;
//		mp=mpMax;
//		}
//
//	public boolean upStrength(int val)
//		{
//		if(statPoints<val)
//			return false;
//		strength+=val;
//		statPoints-=val;
//		return true;
//		}
//
//	public boolean upConstitution(int val)
//		{
//		if(statPoints<val)
//			return false;
//		constitution+=val;
//		statPoints-=val;
//		updateHp();
//		return true;
//		}
//
//	public boolean upIntelligence(int val)
//		{
//		if(statPoints<val)
//			return false;
//		intelligence+=val;
//		statPoints-=val;
//		updateMp();
//		return true;
//		}
//
//	public boolean upConcentration(int val)
//		{
//		if(statPoints<val)
//			return false;
//		concentration+=val;
//		statPoints-=val;
//		return true;
//		}
//
//	public boolean upDexterity(int val)
//		{
//		if(statPoints<val)
//			return false;
//		dexterity+=val;
//		statPoints-=val;
//		return true;
//		}
//
//	public boolean addXp(int amount)
//		{
//		xp+=amount;
////		if(World.xpForLevel(level)<=xp)
////			{
////			level++;
////			skillPoints++;
////			statPoints+=3;
////			return true;
////			}
//		return false;
//		}
//
//	public boolean addItem(Item i)
//		{
//		inventory.add(i);
//		return true;
//		}
//
//	public int inventorySize()
//		{
//		return inventory.size();
//		}
//
//	public Item getItem(int i)
//		{
//		return inventory.get(i);
//		}
//
//	public boolean removeItem(int i)
//		{
//		inventory.remove(i);
//		return true;
//		}
//
//	public int skillPoints()
//		{
//		return skillPoints;
//		}
//
//	public int statPoints()
//		{
//		return statPoints;
//		}
//
//	public Set<SkillInterface> skills()
//		{
//		return skillsLevel.keySet();
//		}
//
//	public int skillLevel(SkillInterface s)
//		{
//		Integer i=skillsLevel.get(s);
//		return i==null?0:i;
//		}
//
//	public int disciplineLevel(Discipline d)
//		{
//		Integer i=disciplinesLevel.get(d);
//		return i==null?0:i;
//		}
//
//	public boolean upSkill(SkillInterface s)
//		{
//		if(skillPoints<1)
//			return false;
//		if(s.type()==Type.PASSIVE)
//			{
//			Effect e=passiveEffect.get(s);
//			if(e!=null)
//				effectsTotal.put(e.type, effectsTotal.get(e.type)-e.value);
//			}
//		skillPoints--;
//		skillsLevel.put(s, skillLevel(s)+1);
////		if(s.type()==Type.PASSIVE)
////			World.cast(this, null, s);
//		return true;
//		}
//
//	public boolean upDiscipline(Discipline d)
//		{
//		if(skillPoints<1)
//			return false;
//		skillPoints--;
//		Integer i=disciplinesLevel.get(d);
//		if(i==null)
//			i=1;
//		else
//			i++;
//		disciplinesLevel.put(d, i);
//		return true;
//		}
//	}
