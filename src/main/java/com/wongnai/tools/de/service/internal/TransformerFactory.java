package com.wongnai.tools.de.service.internal;

import com.wongnai.tools.de.settings.Transformation;

/**
 * Transformer factory.
 *
 * @author Suparit Krityakien
 */
public class TransformerFactory {
	/**
	 * Creates from transformation setting.
	 *
	 * @param t
	 *            transformation setting
	 * @return transformer
	 */
	public Transformer create(Transformation t) {
		if ("email".equalsIgnoreCase(t.getType())) {
			return EmailTransformer.create(t);
		} else {
			return RegExTransformer.create(t);
		}
	}
}
