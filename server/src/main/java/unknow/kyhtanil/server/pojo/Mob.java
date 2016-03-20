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

import unknow.kyhtanil.common.pojo.*;
import unknow.kyhtanil.server.pojo.Item.Slot;
import unknow.kyhtanil.server.pojo.Item.Type;

public class Mob /*extends Actor*/implements MobInterface
	{
	protected String name;
	protected int level;

	/* basic stat */
	protected int strength;
	protected int constitution;
	protected int intelligence;
	protected int concentration;
	protected int dexterity;

	/* stat calculated from basic stat */
	protected int hp;
	protected int hpMax;
	protected int mp;
	protected int mpMax;

	protected Set<Buff> buffs=new HashSet<>();
	protected Map<SkillInterface,Effect> passiveEffect=new HashMap<>();
	protected Map<EffectType,Integer> effectsTotal=new HashMap<>();

	protected Damage damage;
	protected Resistance resistance;

	protected int gold;

	protected Weapon main_hand;
	protected Item off_hand;
	protected Armor helm;
	protected Armor chest;
	protected Armor gloves;
	protected Armor legs;
	protected Armor boots;

	protected Mob()
		{
		for(EffectType e:EffectType.values())
			effectsTotal.put(e, 0);
		damage=new Damage(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		resistance=new Resistance(0, Resistance.Quality.NORMAL, Resistance.Quality.NORMAL, Resistance.Quality.NORMAL, Resistance.Quality.NORMAL, Resistance.Quality.NORMAL, Resistance.Quality.NORMAL);
		}

	public Mob(String name, int level, int strength, int constitution, int intelligence, int concentration, int dexterity)
		{
		this();
		this.name=name;
		this.level=level;
		this.strength=strength;
		this.constitution=constitution;
		this.intelligence=intelligence;
		this.concentration=concentration;
		this.dexterity=dexterity;
		hpMax=mpMax=0;
		updateHp();
		updateMp();
		}

	/** recalculate Hp after constitution modification */
	protected void updateHp()
		{
		double rate=(hpMax==0?1:hp/hpMax);
		hpMax=constitution*15+10;
		hp=(int)(hpMax*rate);
		}

	/** recalculate Mp after intelligence modification */
	protected void updateMp()
		{
		double rate=(mpMax==0?1:mp/mpMax);
		mpMax=intelligence*9+10;
		mp=(int)(mpMax*rate);
		}

	public void looseHp(int dmg)
		{
		hp-=dmg;
		}

	public void looseMp(int cost)
		{
		mp-=cost;
		}

	public String name()
		{
		return name;
		}

	public int hp()
		{
		return hp;
		}

	public int maxHp()
		{
		return hpMax;
		}

	public int mp()
		{
		return mp;
		}

	public int maxMp()
		{
		return mpMax;
		}

	public int strengthTotal()
		{
		return strength+effectsTotal.get(EffectType.STAT_STR);
		}

	public int constitutionTotal()
		{
		return constitution+effectsTotal.get(EffectType.STAT_CONS);
		}

	public int intelligenceTotal()
		{
		return intelligence+effectsTotal.get(EffectType.STAT_INT);
		}

	public int concentrationTotal()
		{
		return concentration+effectsTotal.get(EffectType.STAT_CONC);
		}

	public int dexterityTotal()
		{
		return dexterity+effectsTotal.get(EffectType.STAT_DEX);
		}

	public int hitChanceTotal()
		{
		return hitChance()+effectsTotal.get(EffectType.STAT_HC);
		}

	public int evadeTotal()
		{
		return evade()+effectsTotal.get(EffectType.STAT_EV);
		}

	public int spellFocusTotal()
		{
		return spellFocus()+effectsTotal.get(EffectType.STAT_SF);
		}

	public int spellResistTotal()
		{
		return spellResist()+effectsTotal.get(EffectType.STAT_SR);
		}

	public int strength()
		{
		return strength;
		}

	public int constitution()
		{
		return constitution;
		}

	public int intelligence()
		{
		return intelligence;
		}

	public int concentration()
		{
		return concentration;
		}

	public int dexterity()
		{
		return dexterity;
		}

	public int hitChance()
		{
		return dexterity*6+concentration*4;
		}

	public int evade()
		{
		return dexterity*2;
		}

	public int spellFocus()
		{
		return concentration*6+intelligence*2;
		}

	public int spellResist()
		{
		return intelligence*2+concentration/2;
		}

	public int carryWeigth()
		{
		return strength*3+20;
		}

	public Resistance resistance()
		{ // TODO
		return resistance;
		}

	public Damage damage()
		{
		return damage;
		}

	public int lvl()
		{
		return level;
		}

	public int gold()
		{
		return gold;
		}

	public double speed()
		{
		double s=rawSpeed()+weaponSpeed();
		return s<0.1?0.1:s;
		}

	public double weaponSpeed()
		{
		if(main_hand==null)
			return 0;
		int dif=strength-main_hand.strReq();
		return main_hand.speed()-(dif>=0?0:Math.log(-dif));
		}

	public double rawSpeed()
		{
		return Math.log(dexterity);
		}

	public void addBuff(Buff buff)
		{
		boolean updateHp=false;
		boolean updateMp=false;
		buffs.add(buff);
		Effect e=buff.effect();
		Integer i=effectsTotal.get(e.type());
		if(i==null)
			i=0;
		effectsTotal.put(e.type(), i+e.value());
		if(e.type()==EffectType.STAT_INT||e.type()==EffectType.STAT_MP)
			updateMp=true;
		if(e.type()==EffectType.STAT_CONS||e.type()==EffectType.STAT_HP)
			updateHp=true;

		if(updateHp)
			updateHp();
		if(updateMp)
			updateMp();
		}

	public void removeBuff(Buff buff)
		{
		if(!buffs.remove(buff))
			return;
		boolean updateHp=false;
		boolean updateMp=false;
		Integer i=effectsTotal.get(buff.effect().type());
		if(i==null)
			i=0;
		effectsTotal.put(buff.effect().type(), i-buff.effect().value());
		if(buff.effect().type()==EffectType.STAT_INT||buff.effect().type()==EffectType.STAT_MP)
			updateMp=true;
		if(buff.effect().type()==EffectType.STAT_CONS||buff.effect().type()==EffectType.STAT_HP)
			updateHp=true;
		if(updateHp)
			updateHp();
		if(updateMp)
			updateMp();
		}

	public void addPassive(SkillInterface skill, Effect e)
		{
		Integer i=effectsTotal.get(e.type);
		effectsTotal.put(e.type, (i==null?0:i)+e.value);
		passiveEffect.put(skill, e);
		}

	public int skillLevel(SkillInterface s)
		{
		return 0;
		}

	public int disciplineLevel(Discipline d)
		{
		return 0;
		}

	protected void unequip(Item i)
		{
		if(i==null)
			return;
		if(i.type()==Type.WEAPON)
			{
			Weapon w=(Weapon)i;
			damage.subAssign(w.damage());
			removeEffects(w.effects());
			}
		else if(i.type()==Type.ARMOR)
			{
			Armor a=(Armor)i;
			resistance.subAssign(a.resistance());
			removeEffects(a.effects());
			}
		}

	private void removeEffects(Set<Effect> effects)
		{
		boolean updateHp=false;
		boolean updateMp=false;

		for(Effect e:effects)
			{
			Integer i=effectsTotal.get(e.type());
			if(i==null)
				i=0;
			effectsTotal.put(e.type(), i-e.value());
			if(e.type()==EffectType.STAT_INT||e.type()==EffectType.STAT_MP)
				updateMp=true;
			if(e.type()==EffectType.STAT_CONS||e.type()==EffectType.STAT_HP)
				updateHp=true;
			}
		if(updateHp)
			updateHp();
		if(updateMp)
			updateMp();
		}

	private void addEffects(Set<Effect> effects)
		{
		if(effects==null)
			return;
		boolean updateHp=false;
		boolean updateMp=false;

		for(Effect e:effects)
			{
			Integer i=effectsTotal.get(e.type());
			if(i==null)
				i=0;
			effectsTotal.put(e.type(), i+e.value());
			if(e.type()==EffectType.STAT_INT||e.type()==EffectType.STAT_MP)
				updateMp=true;
			if(e.type()==EffectType.STAT_CONS||e.type()==EffectType.STAT_HP)
				updateHp=true;
			}
		if(updateHp)
			updateHp();
		if(updateMp)
			updateMp();
		}

	public void unequip(Slot s)
		{
		unequip(getEquip(s));
		switch (s)
			{
			case MAIN_HAND:
				main_hand=null;
				break;
			case OFF_HAND:
				off_hand=null;
				break;
			case HELM:
				helm=null;
				break;
			case CHEST:
				chest=null;
				break;
			case GLOVE:
				gloves=null;
				break;
			case LEGS:
				legs=null;
				break;
			case BOOTS:
				boots=null;
				break;
			case NONE:
				break;
			}
		}

	public boolean equip(Item i)
		{
		if(i==null||i.slot()==null)
			return false;

		if(i.type()==Type.WEAPON)
			{
			Weapon w=(Weapon)i;
			if(i.slot()==Slot.MAIN_HAND)
				main_hand=w;
			else if(i.slot()==Slot.OFF_HAND)
				off_hand=w;
			else
				return false;
			damage.addAssign(w.damage());
			addEffects(w.effects());
			}
		else if(i.type()==Type.ARMOR)
			{
			Armor a=(Armor)i;
			switch (i.slot())
				{
				case OFF_HAND:
					off_hand=a;
					break;
				case HELM:
					helm=a;
					break;
				case CHEST:
					chest=a;
					break;
				case GLOVE:
					gloves=a;
					break;
				case LEGS:
					legs=a;
					break;
				case BOOTS:
					boots=a;
					break;
				default:
					return false;
				}
			resistance.addAssign(a.resistance());
			addEffects(a.effects());
			}
		return true;
		}

	public Item getEquip(Slot slot)
		{
		switch (slot)
			{
			case MAIN_HAND:
				return main_hand;
			case OFF_HAND:
				return off_hand;
			case HELM:
				return helm;
			case CHEST:
				return chest;
			case GLOVE:
				return gloves;
			case LEGS:
				return legs;
			case BOOTS:
				return boots;
			case NONE:
				return null;
			}
		return null; // will never be here but well eclipse is dumb
		}

//	public double distance(float x, float y)
//		{
//		return Math.sqrt((this.x-x)*(this.x-x)+(this.y-y)*(this.y-y));
//		}

	public String toString()
		{
		return name+" ("+level+")";
		}
	}
