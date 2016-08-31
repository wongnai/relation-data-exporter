package com.wongnai.tools.de.model;

import java.util.List;

/**
 * Columns.
 *
 * @author Suparit Krityakien
 */
public interface Columns extends List<Column> {
	/**
	 * Gets column by name.
	 *
	 * @param name
	 *            name
	 * @return column
	 */
	Column get(String name);

	/**
	 * Gets selection text.
	 *
	 * @param alias
	 *            table alias
	 * @return selection text
	 */
	String getSelectionText(String alias);

	/**
	 * Gets primary key.
	 *
	 * @return primary key
	 */
	List<Column> getPrimaryKeys();
}
