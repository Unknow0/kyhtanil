/**
 * 
 */
package unknow.kyhtanil.client.i18n;

import java.text.ChoiceFormat;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import unknow.kyhtanil.client.i18n.MessageFormat.Compound;
import unknow.kyhtanil.client.i18n.MessageFormat.FieldFormat;
import unknow.kyhtanil.client.i18n.MessageFormat.Static;
import unknow.kyhtanil.client.i18n.MessageFormat.ToString;

/**
 * @author unknow
 */
public class I18NParser {
	private static final MessageFormat[] EMPTY = new MessageFormat[0];
	private static final int TEXT = 0;
	private static final int INDEX = 1;
	private static final int TYPE = 2;
	private static final int MOD = 3;
	private static final int QUOTE = 4;

	private StringBuilder sb = new StringBuilder();
	private List<MessageFormat> format = new ArrayList<>();

	private Locale locale;

	public MessageFormat parse(String input) {
		int index = -1;
		String type = null;
		int s = TEXT;
		int len = input.length();
		for (int i = 0; i < len; i++) {
			char c = input.charAt(i);
			if ((s == TEXT || s == QUOTE) && c == '\'') {
				c = input.charAt(i + 1);

				// check duplicate quote
				if (c == '\'') {
					i++;
					sb.append('\'');
				} else
					s = s == QUOTE ? TEXT : QUOTE;
			} else if (s == TEXT && c == '{') {
				format.add(new Static(sb.toString()));
				sb.setLength(0);
				s = INDEX;
			} else if (s == INDEX && (c == ',' || c == '}')) {
				index = Integer.parseInt(sb.toString());
				sb.setLength(0);
				if (c == '}') {
					build(index, type, null);
					type = null;
					s = TEXT;
				} else
					s = TYPE;
			} else if (s == TYPE && (c == ',' || c == '}')) {
				type = sb.toString();
				sb.setLength(0);
				if (c == '}') {
					build(index, type, null);
					type = null;
					s = TEXT;
				} else
					s = MOD;
			} else if (s == MOD && c == '}') {
				build(index, type, sb.toString());
				sb.setLength(0);
				type = null;
				s = TEXT;
			} else
				sb.append(c);
		}
		if (sb.length() > 0) {
			if (s != TEXT)
				throw new RuntimeException(s == QUOTE ? "unclused quote" : "unclosed brace");
			format.add(new Static(sb.toString()));
		}
		if (format.size() == 1)
			return format.get(0);
		return new Compound(format.toArray(EMPTY));
	}

	private void build(int index, String type, String mod) {
		MessageFormat f = getFormat(index, type, mod);
		if (f == null)
			throw new RuntimeException("no formater found for '" + type + "'");
		format.add(f);
	}

	protected MessageFormat getFormat(int index, String type, String mod) {
		if (type == null)
			return new ToString(index);
		if ("number".equals(type)) {
			if (mod == null)
				return new FieldFormat(index, NumberFormat.getInstance(locale));
			if ("currency".equals(type))
				return new FieldFormat(index, NumberFormat.getCurrencyInstance(locale));
			if ("percent".equals(type))
				return new FieldFormat(index, NumberFormat.getPercentInstance(locale));
			if ("interger".equals(type))
				return new FieldFormat(index, NumberFormat.getIntegerInstance(locale));
			return new FieldFormat(index, new DecimalFormat(mod, DecimalFormatSymbols.getInstance(locale)));
		}
		if ("date".equals(type)) {
			if (mod == null)
				return new FieldFormat(index, DateFormat.getDateInstance(DateFormat.DEFAULT, locale));
			if ("short".equals(type))
				return new FieldFormat(index, DateFormat.getDateInstance(DateFormat.SHORT, locale));
			if ("medium".equals(type))
				return new FieldFormat(index, DateFormat.getDateInstance(DateFormat.DEFAULT, locale));
			if ("long".equals(type))
				return new FieldFormat(index, DateFormat.getDateInstance(DateFormat.LONG, locale));
			if ("full".equals(type))
				return new FieldFormat(index, DateFormat.getDateInstance(DateFormat.FULL, locale));
			return new FieldFormat(index, new SimpleDateFormat(mod, locale));
		}
		if ("time".equals(type)) {
			if (mod == null)
				return new FieldFormat(index, DateFormat.getTimeInstance(DateFormat.DEFAULT, locale));
			if ("short".equals(type))
				return new FieldFormat(index, DateFormat.getTimeInstance(DateFormat.SHORT, locale));
			if ("medium".equals(type))
				return new FieldFormat(index, DateFormat.getTimeInstance(DateFormat.MEDIUM, locale));
			if ("long".equals(type))
				return new FieldFormat(index, DateFormat.getTimeInstance(DateFormat.LONG, locale));
			if ("full".equals(type))
				return new FieldFormat(index, DateFormat.getTimeInstance(DateFormat.FULL, locale));
			return new FieldFormat(index, new SimpleDateFormat(mod, locale));
		}
		if ("choice".equals(type))
			return new FieldFormat(index, new ChoiceFormat(mod));
		return null;
	}

}
