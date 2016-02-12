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

import unknow.kyhtanil.server.pojo.Item.*;

public class ArmorStub
	{
	private int lvl;

	private Resistance resistance;
	private int strReq;

	private EffectStub[] allowed;

	private String name;
	private String desc;
	private double weight;

	private int id;
	private Item.Slot slot;

	public ArmorStub(int i, String n, String d, int l, Resistance res, int str, double w, Item.Slot sl, Set<EffectStub> effects)
		{
		id=i;
		name=n;
		desc=d;
		lvl=l;
		resistance=res;
		strReq=str;
		weight=w;
		slot=sl;
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
	public Armor generate(int nbr)
		{
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

		return new Armor(this, e);
		}

	public double weight()
		{
		return weight;
		}

	public Resistance resistance()
		{
		return resistance;
		}

	public int level()
		{
		return lvl;
		}

	public int id()
		{
		return id;
		}

	public Item.Slot slot()
		{
		return slot;
		}

	public int strReq()
		{
		return strReq;
		}

	public Object effectCount()
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

	public void setLevel(int l)
		{
		lvl=l;
		}

	public void setSlot(Slot v)
		{
		slot=v;
		}

	public void setDesc(String d)
		{
		desc=d;
		}
	}
