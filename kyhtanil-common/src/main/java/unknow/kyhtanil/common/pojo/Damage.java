/*******************************************************************************
 * Copyright (c) 2014 Unknow. All rights reserved. This program and the accompanying materials are made available under the terms of the GNU Lesser Public License v3 which accompanies this distribution, and is available at http://www.gnu.org/licenses/lgpl-3.0.html
 * 
 * Contributors: Unknow - initial API and implementation
 ******************************************************************************/
package unknow.kyhtanil.common.pojo;

/**
 * 
 * @author Unknow
 */
public class Damage {
	public int slashingMin;
	public int slashingRng;
	public int bluntMin;
	public int bluntRng;
	public int piercingMin;
	public int piercingRng;
	public int lightningMin;
	public int lightningRng;
	public int fireMin;
	public int fireRng;
	public int iceMin;
	public int iceRng;

	public Damage() {
	}

	public Damage(int s, int b, int p, int f, int i, int l) {
		this(s, 0, b, 0, p, 0, f, 0, i, 0, l, 0);
	}

	public Damage(int s, int s_r, int b, int b_r, int p, int p_r, int f, int f_r, int l, int l_r, int i, int i_r) {
		set(s, s_r, b, b_r, p, p_r, f, f_r, l, l_r, i, i_r);
	}

	public Damage mult(double i) {
		return new Damage((int) (slashingMin * i), (int) (slashingRng * i), (int) (bluntMin * i), (int) (bluntRng * i), (int) (piercingMin * i), (int) (piercingRng * i), (int) (fireMin * i), (int) (fireRng * i), (int) (iceMin * i), (int) (iceRng * i), (int) (lightningMin * i), (int) (lightningRng * i));
	}

	public Damage add(Damage d) {
		return new Damage(slashingMin + d.slashingMin, slashingRng + d.slashingRng, bluntMin + d.bluntMin, bluntRng + d.bluntRng, piercingMin + d.piercingMin, piercingRng + d.piercingRng, fireMin + d.fireMin, fireRng + d.fireRng, iceMin + d.iceMin, iceRng + d.iceRng, lightningMin + d.lightningMin, lightningRng + d.lightningRng);
	}

	public Damage sub(Damage d) {
		return new Damage(slashingMin - d.slashingMin, slashingRng - d.slashingRng, bluntMin - d.bluntMin, bluntRng - d.bluntRng, piercingMin - d.piercingMin, piercingRng - d.piercingRng, fireMin - d.fireMin, fireRng - d.fireRng, iceMin - d.iceMin, iceRng - d.iceRng, lightningMin - d.lightningMin, lightningRng - d.lightningRng);
	}

	public void multAssign(double i) {
		slashingMin *= i;
		slashingRng *= i;
		bluntMin *= i;
		bluntRng *= i;
		piercingMin *= i;
		piercingRng *= i;
		fireMin *= i;
		fireRng *= i;
		iceMin *= i;
		iceRng *= i;
		lightningMin *= i;
		lightningRng *= i;
	}

	public void addAssign(Damage d) {
		slashingMin += d.slashingMin;
		slashingRng += d.slashingRng;
		bluntMin += d.bluntMin;
		bluntRng += d.bluntRng;
		piercingMin += d.piercingMin;
		piercingRng += d.piercingRng;
		fireMin += d.fireMin;
		fireRng += d.fireRng;
		iceMin += d.iceMin;
		iceRng += d.iceRng;
		lightningMin += d.lightningMin;
		lightningRng += d.lightningRng;
	}

	public void subAssign(Damage d) {
		slashingMin -= d.slashingMin;
		slashingRng -= d.slashingRng;
		bluntMin -= d.bluntMin;
		bluntRng -= d.bluntRng;
		piercingMin -= d.piercingMin;
		piercingRng -= d.piercingRng;
		fireMin -= d.fireMin;
		fireRng -= d.fireRng;
		iceMin -= d.iceMin;
		iceRng -= d.iceRng;
		lightningMin -= d.lightningMin;
		lightningRng -= d.lightningRng;
	}

	public int rawMin() {
		return slashingMin + bluntMin + piercingMin + fireMin + iceMin + lightningMin;
	}

	public int rawMax() {
		return rawMin() + slashingRng + bluntRng + piercingRng + fireRng + iceRng + lightningRng;
	}

	public void set(int s, int s_r, int b, int b_r, int p, int p_r, int f, int f_r, int l, int l_r, int i, int i_r) {
		slashingMin = s;
		slashingRng = s_r;
		bluntMin = b;
		bluntRng = b_r;
		piercingMin = p;
		piercingRng = p_r;
		fireMin = f;
		fireRng = f_r;
		iceMin = i;
		iceRng = i_r;
		lightningMin = l;
		lightningRng = l_r;
	}

	public void reset() {
		set(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
	}
}
