package com.wongnai.tools.de.model;

/**
 * Reference.
 *
 * @author Suparit Krityakien
 */
public interface Reference {
	/**
	 * Gets column.
	 *
	 * @return column
	 */
	Column getColumn();

	/**
	 * Gets key.
	 *
	 * @return key
	 */
	Column getKey();
}
