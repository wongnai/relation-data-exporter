package com.wongnai.tools.de.service.internal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.wongnai.tools.de.settings.Transformation;

/**
 * An implementation of {@link Transformer} which uses regular expression to
 * replace value.
 *
 * @author Suparit Krityakien
 */
public class RegExTransformer implements Transformer {
	private Pattern fromPattern;
	private String toPattern;

	@Override
	public Object apply(Object o) {
		String s = o.toString();

		Matcher m = fromPattern.matcher(s);
		if (m.matches()) {
			s = m.replaceAll(toPattern);
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
		RegExTransformer t = new RegExTransformer();
		t.fromPattern = Pattern.compile(transformation.getFrom());
		t.toPattern = transformation.getTo();

		return t;
	}
}
