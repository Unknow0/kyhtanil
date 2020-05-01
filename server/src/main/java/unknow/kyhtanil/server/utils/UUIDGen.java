package unknow.kyhtanil.server.utils;

import unknow.kyhtanil.common.pojo.UUID;
import unknow.kyhtanil.server.Cfg;

/**
 * generate uuid for entities
 * 
 * @author unknow
 */
public class UUIDGen {
	private final UUID self;
	private long msb = 0;
	private long lsb = 0;
	private final byte nodeId;
	private final Object monitor = new Object();

	/**
	 * create new UUIDGen
	 */
	public UUIDGen() {
		this.nodeId = Cfg.nodeId;
		self = next();
	}

	/**
	 * @return uuid for this node
	 */
	public UUID self() {
		return self;
	}

	/**
	 * @return the next uuid
	 */
	public UUID next() {
		synchronized (monitor) {
			lsb++;
			if (lsb == 0)
				msb++;
			if (msb > 0xFFFFFFFFFFFFFFL)
				msb = 0;
			byte[] b = new byte[16];

			b[0] = nodeId;
			b[1] = (byte) ((msb >> 48) & 0xFF);
			b[2] = (byte) ((msb >> 40) & 0xFF);
			b[3] = (byte) ((msb >> 32) & 0xFF);
			b[4] = (byte) ((msb >> 24) & 0xFF);
			b[5] = (byte) ((msb >> 16) & 0xFF);
			b[6] = (byte) ((msb >> 8) & 0xFF);
			b[7] = (byte) ((msb) & 0xFF);
			b[8] = (byte) ((lsb >> 56) & 0xFF);
			b[9] = (byte) ((lsb >> 48) & 0xFF);
			b[10] = (byte) ((lsb >> 40) & 0xFF);
			b[11] = (byte) ((lsb >> 32) & 0xFF);
			b[12] = (byte) ((lsb >> 24) & 0xFF);
			b[13] = (byte) ((lsb >> 16) & 0xFF);
			b[14] = (byte) ((lsb >> 8) & 0xFF);
			b[15] = (byte) ((lsb) & 0xFF);

			return new UUID(b);
		}
	}
}
