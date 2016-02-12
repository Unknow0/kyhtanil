package unknow.kyhtanil.client;

import java.io.*;

public class MapModel
	{
	protected int width, height;
	protected byte[] map;

	public MapModel(int width, int height)
		{
		this.width=width;
		this.height=height;
		this.map=new byte[width*height];
		}

	/** @retrun the id at these coord */
	public int get(int x, int y)
		{
		return map[x+y*width];
		}

	/** width of the map in tile */
	public int width()
		{
		return width;
		}

	/** heigth of the map in tile */
	public int height()
		{
		return height;
		}

	public void load(DataInputStream in) throws IOException
		{
		width=in.readInt();
		height=in.readInt();
		map=new byte[width*height];
		in.read(map);
		}

	public void save(DataOutputStream out) throws IOException
		{
		out.writeInt(width);
		out.writeInt(height);
		out.write(map);
		}

	public void set(int x, int y, byte i)
		{
		map[x+y*width]=i;
		}

	public void setSize(int nw, int nh)
		{
		byte[] nmap=new byte[nw*nh];

		int mw=nw>width?width:nw;
		int mh=nh>height?height:nh;

		for(int i=0; i<mh; i++)
			System.arraycopy(map, i*width, nmap, i*nw, mw);

		width=nw;
		height=nh;
		map=nmap;
		}
	}
