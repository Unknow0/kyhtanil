/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package unknow.kyhtanil.common.pojo;

import java.util.Arrays;
import java.util.Formatter;

public class UUID implements Comparable<UUID> {
	public byte[] bytes;

	/** Creates a new UUID */
	public UUID() {
	}

	/**
	 * Creates a new UUID with the given bytes.
	 * 
	 * @param bytes The bytes to create the new UUID.
	 */
	public UUID(byte[] bytes) {
		this.bytes = bytes;
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(bytes);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj instanceof UUID)
			return Arrays.equals(bytes, ((UUID) obj).bytes);
		if (obj instanceof byte[])
			return Arrays.equals(bytes, (byte[]) obj);
		return false;
	}

	@Override
	public String toString() {
		try (Formatter fmt = new Formatter()) {
			for (int i = 0; i < bytes.length; i++)
				fmt.format("%02x", bytes[i]);
			return fmt.toString();
		}
	}

	@Override
	public int compareTo(UUID o) {
		if (this == o)
			return 0;
		byte[] b1 = bytes;
		byte[] b2 = o.bytes;

		int s = b1.length - b2.length;
		if (s != 0)
			return s;
		for (int i = b1.length - 1; i >= 0; i--) {
			s = b1[i] - b2[i];
			if (s != 0)
				return s;
		}
		return 0;
	}
}