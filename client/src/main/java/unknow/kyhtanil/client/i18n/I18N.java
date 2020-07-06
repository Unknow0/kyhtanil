/**
 * 
 */
package unknow.kyhtanil.client.i18n;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import unknow.common.data.kvstore.StringIndex;

/**
 * @author unknow
 */
public class I18N {
	private static I18NParser parser = new I18NParser();

	private static StringIndex index;

	private static StringBuilder sb = new StringBuilder();

	private I18N() {
	}

	public static void load(Locale locale) throws IOException {
		parser.setLocale(locale);
		index = new StringIndex(new File("data", "text." + locale.getLanguage()));
	}

	public static String get(CharSequence key) {
		try {
			return index.get(key.toString());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static String format(CharSequence key, Object... arg) {
		String string = get(key);
		if (string == null)
			return "";
		sb.setLength(0);
		parser.parse(string).format(sb, arg);
		return sb.toString();
	}

	public interface Source {
		String get(String key);
	}
}
