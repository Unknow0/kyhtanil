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
package unknow.kyhtanil.server;

import unknow.kyhtanil.server.pojo.*;
import bsh.*;

/**
 * 
 * @author Unknow
 */
public class World
	{
	private final Interpreter bsh=new Interpreter();

	protected final static World world=new World();

	protected World()
		{
		try
			{
			bsh.set("world", this);
			bsh.eval("import unknow.kyhtanil.api.*");
			}
		catch (EvalError e)
			{
			e.printStackTrace();
			}
		}

	public static long xpForLevel(int lvl)
		{
		return 100*lvl*(lvl+1)*(2*lvl+1)/6;
		}

	public static boolean isEvade(MobInterface att, MobInterface def)
		{
		return (Math.random()*att.hitChance())/def.evade()>0.75;
		}

	public static boolean isResit(MobInterface att, MobInterface def)
		{
		return (Math.random()*att.spellFocus())/def.spellResist()>0.75;
		}

	public static int damage(Damage dmg, Resistance res)
		{

		return (int)((dmg.bluntMin+dmg.bluntRng*Math.random())/Math.log(res.blunt()*5)+(dmg.piercingMin+dmg.piercingRng*Math.random())/Math.log(res.piercing()*5)+(dmg.slashingMin+dmg.slashingRng*Math.random())/Math.log(res.slashing()*5)+(dmg.fireMin+dmg.fireRng*Math.random())/Math.log(res.fire()*5)+(dmg.iceMin+dmg.iceRng*Math.random())/Math.log(res.ice()*5)+(dmg.lightningMin+dmg.lightningRng*Math.random())/Math.log(res.lightning()*5));
		}

	/**
	 * att hit def
	 * @return null if def evade, else return damage done.
	 */
	public static Integer hit(MobInterface att, MobInterface def)
		{
		if(isEvade(att, def))
			return null;
		int dmg=damage(att.damage(), def.resistance());
		def.looseHp(dmg);
		return dmg;
		}

	/**
	 * cast a spell
	 * @return null if target resist, -1 if spell don't do damage else return damage done.
	 */
	public static Integer cast(MobInterface caster, MobInterface target, SkillInterface s)
		{
		if(target!=null&&isResit(caster, target))
			return null;

		try
			{
			world.bsh.set("caster", caster);
			world.bsh.set("target", target);
			world.bsh.set("skill", s);
			world.bsh.set("skillLevel", caster.skillLevel(s));
			world.bsh.set("disciplineLevel", caster.disciplineLevel(s.discipline()));

			Integer i=(Integer)world.bsh.eval(s.action());
			return i;
			}
		catch (EvalError e)
			{
			e.printStackTrace(System.err);
			return null;
			}
		}
	}
