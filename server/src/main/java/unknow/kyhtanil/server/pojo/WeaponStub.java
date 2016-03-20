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

public class WeaponStub
	{
	public static enum Type
		{
		AXE, SWORD, MACE, WAND, STAVE, BOW, DAGGER
		}

	private int lvl;

	private Damage damage;
	private int strReq;
	private double speed;

	private Type type;

	private EffectStub[] allowed;

	private String name;
	private String desc;
	private double weight;

	private int id;
	private Item.Slot slot;

	public WeaponStub()
		{
		}

	public WeaponStub(int i, String n, String d, int l, Damage dmg, int str, double s, double w, Item.Slot sl, Type t, Set<EffectStub> effects)
		{
		id=i;
		name=n;
		desc=d;
		lvl=l;
		damage=dmg;
		strReq=str;
		speed=s;
		weight=w;
		slot=sl;
		type=t;
		allowed=effects.toArray(new EffectStub[0]);
		}

	public String name()
		{
		return name;
		}

	public String desc()
		{
		return desc;
		}

	/**
	 * generate a weapon with nbr effect.
	 */
	public Weapon generate(int nbr)
		{
		if(nbr>allowed.length)
			nbr=allowed.length;
		Set<Effect> e=new HashSet<>();
		Set<Integer> set=new HashSet<>();
		for(int i=0; i<nbr; i++) // TODO update this algo
			{
			int id;
			do
				{
				id=(int)(Math.random()*allowed.length);
				}
			while (set.contains(id));
			set.add(id);
			e.add(allowed[id].generate());
			}

		return new Weapon(this, e);
		}

	public double weight()
		{
		return weight;
		}

	public Damage damage()
		{
		return damage;
		}

	public int level()
		{
		return lvl;
		}

	public int strReq()
		{
		return strReq;
		}

	public int id()
		{
		return id;
		}

	public double speed()
		{
		return speed;
		}

	public Item.Slot slot()
		{
		return slot;
		}

	public int effectCount()
		{
		return allowed.length;
		}

	public void setId(int i)
		{
		id=i;
		}

	public void setName(String s)
		{
		name=s;
		}

	public void setDesc(String d)
		{
		desc=d;
		}

	public void setLevel(int l)
		{
		lvl=l;
		}

	public void setStrReq(int s)
		{
		strReq=s;
		}

	public void setSlot(Slot v)
		{
		slot=v;
		}

	public void setDamage(Damage dmg)
		{
		damage=dmg;
		}

	public void setType(String s)
		{
		type=Type.valueOf(s);
		}

	public void setSpeed(double s)
		{
		speed=s;
		}
	}
