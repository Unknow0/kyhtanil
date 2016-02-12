/*******************************************************************************
 * Copyright (c) 2014 Unknow.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.html
 * 
 * Contributors:
 *     Unknow - initial API and implementation
 ******************************************************************************/
package unknow.kyhtanil.server.pojo;

import java.io.*;
import java.util.*;

public class Armor extends Item implements Serializable
	{
	private static final long serialVersionUID=1L;
	protected ArmorStub stub;
	protected Set<Effect> effects;
	protected Damage total;
	
	public Armor() {}
	public Armor(ArmorStub s, Set<Effect> e)
		{
		super(s.id(), s.name(), s.desc(), s.weight(), s.slot());
		stub=s;
		effects=e;
		}

	public Resistance resistance()
		{
		return stub.resistance();
		}

	public Type type()
		{
		return Type.ARMOR;
		}

	public Set<Effect> effects()
		{
		return effects;
		}
	}
