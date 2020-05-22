/**
 * 
 */
package unknow.kyhtanil.client.i18n;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.badlogic.gdx.Gdx;

/**
 * @author unknow
 */
public class I18N {
	private static I18NParser parser = new I18NParser();

	private Map<String, Source> sources = new HashMap<>();

	private StringBuilder sb = new StringBuilder();

	public I18N() throws IOException {
		final Properties p = new Properties();
		p.load(Gdx.files.internal("text.properties").reader());
		sources.put("ui", p::getProperty);
	}

	public String get(String key) {
		int i = key.indexOf('/');
		Source source = sources.get(key.substring(0, i));
		return source == null ? null : source.get(key.substring(i + 1));
	}

	public String format(String key, Object... arg) {
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
