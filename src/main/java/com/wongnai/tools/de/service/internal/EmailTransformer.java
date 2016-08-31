package com.wongnai.tools.de.service.internal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.wongnai.tools.de.settings.Transformation;

/**
 * An implementation of {@link Transformer} which transforms email randomly.
 *
 * @author Suparit Krityakien
 */
public class EmailTransformer implements Transformer {
	private Pattern fromPattern;
	private int count;
	private String[] addressParts;

	@Override
	public Object apply(Object o) {
		String s = o.toString();

		Matcher m = fromPattern.matcher(s);
		if (m.matches()) {
			s = addressParts[0] + (++count) + "@" + addressParts[1];
		}

		return s;
	}

	/**
	 * Creates transformer
	 *
	 * @param transformation
	 *            transformation
	 * @return transformer
	 */
	public static Transformer create(Transformation transformation) {
		EmailTransformer t = new EmailTransformer();
		t.fromPattern = Pattern.compile(transformation.getFrom());
		t.addressParts = transformation.getTo().split("@");

		return t;
	}
}
