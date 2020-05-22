/**
 * 
 */
package unknow.kyhtanil.client.i18n;

/**
 * @author unknow
 */
public interface MessageFormat {
	void format(StringBuilder sb, Object... arg);

	public static class Compound implements MessageFormat {
		private final MessageFormat[] f;

		public Compound(MessageFormat[] f) {
			this.f = f;
		}

		@Override
		public void format(StringBuilder sb, Object... arg) {
			int len = f.length;
			for (int i = 0; i < len; i++)
				f[i].format(sb, arg);
		}
	}

	public static class Static implements MessageFormat {
		private final String str;

		public Static(String str) {
			this.str = str;
		}

		@Override
		public void format(StringBuilder sb, Object... arg) {
			sb.append(str);
		}

		@Override
		public String toString() {
			return "'" + str + "'";
		}
	}

	public static class ToString implements MessageFormat {
		private final int index;

		public ToString(int index) {
			this.index = index;
		}

		@Override
		public void format(StringBuilder sb, Object... arg) {
			sb.append(arg[index]);
		}

		@Override
		public String toString() {
			return "{" + index + "}";
		}
	}

	public static class FieldFormat implements MessageFormat {
		private final int index;
		private final java.text.Format fmt;

		public FieldFormat(int index, java.text.Format fmt) {
			this.index = index;
			this.fmt = fmt;
		}

		@Override
		public void format(StringBuilder sb, Object... arg) {
			sb.append(fmt.format(arg[index]));
		}

		@Override
		public String toString() {
			return "{" + index + "," + fmt + "}";
		}
	}
}
