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

import java.io.*;
import java.util.*;

public class Item implements Serializable
	{
	private static final long serialVersionUID=1L;

	public static enum Type
		{
		ITEM, WEAPON, ARMOR
		};

	public static enum Slot
		{
		MAIN_HAND, OFF_HAND, TWO_HAND, HELM, GLOVE, CHEST, BOOTS, LEGS, NONE
		};

	protected String name;
	protected String desc;
	protected double weight;
	protected Slot slot;
	protected int id;

	public Item()
		{
		}

	public Item(int i, String n, String d, double w, Slot s)
		{
		id=i;
		name=n;
		desc=d;
		weight=w;
		slot=s;
		}

	public String name()
		{
		return name;
		}

	public String desc()
		{
		return desc;
		}

	public Type type()
		{
		return Type.ITEM;
		}

	public Slot slot()
		{
		return slot;
		}

	public int id()
		{
		return id;
		}

	public void setId(int i)
		{
		id=i;
		}

	public void setSlot(Slot s)
		{
		slot=s;
		}

	public void setName(String n)
		{
		name=n;
		}

	public void setDesc(String d)
		{
		desc=d;
		}

	public void setWeight(int w)
		{
		weight=w;
		}

	public Set<Effect> effects()
		{
		return null;
		}
	}
