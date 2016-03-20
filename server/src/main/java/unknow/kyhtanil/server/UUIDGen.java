package unknow.kyhtanil.server;

import unknow.kyhtanil.common.*;
import unknow.kyhtanil.common.pojo.*;

public class UUIDGen
	{
	private static long msb=1;
	private static long lsb=0;
	private static final Object monitor=new Object();

	public static UUID next()
		{
		synchronized (monitor)
			{
			lsb++;
			if(lsb==0)
				msb++;
			byte[] b=new byte[16];

			b[0]=(byte)((msb>>56)&0xFF);
			b[1]=(byte)((msb>>48)&0xFF);
			b[2]=(byte)((msb>>40)&0xFF);
			b[3]=(byte)((msb>>32)&0xFF);
			b[4]=(byte)((msb>>24)&0xFF);
			b[5]=(byte)((msb>>16)&0xFF);
			b[6]=(byte)((msb>>8)&0xFF);
			b[7]=(byte)((msb)&0xFF);
			b[8]=(byte)((lsb>>56)&0xFF);
			b[9]=(byte)((lsb>>48)&0xFF);
			b[10]=(byte)((lsb>>40)&0xFF);
			b[11]=(byte)((lsb>>32)&0xFF);
			b[12]=(byte)((lsb>>24)&0xFF);
			b[13]=(byte)((lsb>>16)&0xFF);
			b[14]=(byte)((lsb>>8)&0xFF);
			b[15]=(byte)((lsb)&0xFF);

			return new UUID(b);
			}
		}
	}
