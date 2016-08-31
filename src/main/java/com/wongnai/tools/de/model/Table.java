package com.wongnai.tools.de.model;

import java.util.List;

/**
 * Table.
 *
 * @author Suparit Krityakien
 */
public interface Table {
	/**
	 * Gets name.
	 *
	 * @return name
	 */
	String getName();

	/**
	 * Gets Columns.
	 *
	 * @return columns
	 */
	Columns getColumns();

	/**
	 * Gets references.
	 *
	 * @return references
	 */
	List<Reference> getReferences();
}
