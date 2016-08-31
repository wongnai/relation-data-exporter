package com.wongnai.tools.de.service.internal;

import java.util.function.Function;

/**
 * Transformer.
 *
 * @author Suparit Krityakien
 */
public interface Transformer extends Function<Object, Object> {
	Transformer EMPTY = o -> o;
}
