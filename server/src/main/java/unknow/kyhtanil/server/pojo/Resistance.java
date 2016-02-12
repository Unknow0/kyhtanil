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

/**
 * 
 * @author Unknow
 */
public class Resistance
	{
	public int base;
	public double slashing;
	public double blunt;
	public double piercing;
	public double fire;
	public double ice;
	public double lightning;

	public Resistance(int ba, Quality s, Quality b, Quality p, Quality f, Quality l, Quality i)
		{
		base=ba;
		slashing=s.rate;
		blunt=b.rate;
		piercing=p.rate;
		fire=f.rate;
		ice=i.rate;
		lightning=l.rate;
		}

	public Resistance()
		{
		}

	public int base()
		{
		return base;
		}

	public double slashing()
		{
		return slashing*base;
		}

	public double blunt()
		{
		return blunt*base;
		}

	public double piercing()
		{
		return piercing*base;
		}

	public double fire()
		{
		return fire*base;
		}

	public double ice()
		{
		return ice*base;
		}

	public double lightning()
		{
		return lightning*base;
		}

	public void mulAssign(double i)
		{
		base*=i;
		}

	public void addAssign(Resistance res)
		{
		base+=res.base;
		blunt=(blunt+res.blunt)/2;
		slashing=(slashing+res.slashing)/2;
		piercing=(piercing+res.piercing)/2;
		fire=(fire+res.fire)/2;
		ice=(ice+res.ice)/2;
		lightning=(lightning+res.lightning)/2;
		}

	public void subAssign(Resistance res)
		{
		base-=res.base;
		blunt=blunt*2-res.blunt;
		slashing=slashing*2-res.slashing;
		piercing=piercing*2-res.piercing;
		fire=fire*2-res.fire;
		ice=ice*2-res.ice;
		lightning=lightning*2-res.lightning;
		}

	public static enum Quality
		{
		EXELENT(4), VERY_GOOD(3), GOOD(1.5), NORMAL(1), BAD(0.5), VERY_BAD(0.33);

		private double rate;

		private Quality(double r)
			{
			rate=r;
			}

		public double value(int v)
			{
			return v*rate;
			}
		}
	}
