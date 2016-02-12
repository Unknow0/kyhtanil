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

public class EffectStub extends Effect
	{
	private int value_rng;

	public EffectStub(EffectType t, int v, int rng)
		{
		super(t, v);
		value_rng=rng;
		}

	public Effect generate()
		{
		return new Effect(type, value+(int)(Math.random()*value_rng));
		}

	public int valueRng()
		{
		return value_rng;
		}

	public void setType(EffectType t)
		{
		type=t;
		}

	public void setValue(int v)
		{
		value=v;
		}

	public void setValueRng(int v)
		{
		value_rng=v;
		}
	}
