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

public class Buff
	{
	private Effect effect;

	private int duration;

	public Buff(int d, Effect e)
		{
		duration=d;
		effect=e;
		}

	public Effect effect()
		{
		return effect;
		}
	}
