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

public class MapGen
	{
	protected int[][] data;
	protected int width;
	protected int heigth;

	public static final int WALL=0x80;

	public void generate(int w, int h, long seed)
		{
		Random rand=new Random(seed);

		if(data==null||data.length<w||data[0]==null||data[0].length<h)
			data=new int[w][h];
		width=w;
		heigth=h;

		for(int i=0; i<width; i++)
			{
			for(int j=0; j<heigth; j++)
				data[i][j]=rand.nextDouble()<0.4?WALL:0;
			}
		int[][] t1=new int[w][h];
		int[][] t2=data;

		for(int v=0; v<4; v++)
			{
			for(int i=0; i<width; i++)
				{
				for(int j=0; j<heigth; j++)
					t1[i][j]=(wall(1, i, j, t2)>=5||wall(2, i, j, t2)<=2)?WALL:0;
				}
			data=t1;
			t1=t2;
			t2=data;
			}

		for(int v=0; v<3; v++)
			{
			for(int i=0; i<width; i++)
				{
				for(int j=0; j<heigth; j++)
					t1[i][j]=wall(1, i, j, t2)>=5?WALL:0;
				}
			data=t1;
			t1=t2;
			t2=data;
			}
		}

	private int wall(int d, int x, int y, int[][] data)
		{
		int w=0;
		for(int i=x-d; i<=x+d; i++)
			{
			for(int j=y-d; j<=y+d; j++)
				{
				if(i<0||i>=width||j<0||j>=heigth)
					w++;
				else
					{
					if((data[i][j]&WALL)==WALL)
						w++;
					}
				}
			}
		return w;
		}

	public void print()
		{
		System.out.print("+");
		for(int i=0; i<width; i++)
			System.out.print("-");
		System.out.println("+");
		for(int j=0; j<heigth; j++)
			{
			System.out.print("|");
			for(int i=0; i<width; i++)
				{
				System.out.print((data[i][j]&WALL)==WALL?"#":" ");
//				System.out.format("%1d", wall(1, i, j, data));
				}
			System.out.println("|");
			}
		System.out.print("+");
		for(int i=0; i<width; i++)
			System.out.print("-");
		System.out.println("+");
		}

	public static void main(String[] arg)
		{
		MapGen map=new MapGen();
		map.generate(80, 50, System.currentTimeMillis());
		map.print();
		}
	}
