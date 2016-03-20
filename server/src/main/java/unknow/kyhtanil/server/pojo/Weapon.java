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

import unknow.kyhtanil.common.pojo.*;

public class Weapon extends Item implements Serializable
	{
	private static final long serialVersionUID=1L;
	protected WeaponStub stub;
	protected Set<Effect> effects;
	protected Damage total;

	public Weapon()
		{
		}

	public Weapon(WeaponStub s, Set<Effect> e)
		{
		super(s.id(), s.name(), s.desc(), s.weight(), s.slot());
		stub=s;
		effects=e;
		}

	public Damage damage()
		{
		return stub.damage();
		}

	public Type type()
		{
		return Type.WEAPON;
		}

	public int lvl()
		{
		return stub.level();
		}

	public int strReq()
		{
		return stub.strReq();
		}

	public double speed()
		{
		return stub.speed();
		}

	public Set<Effect> effects()
		{
		return effects;
		}
	}
