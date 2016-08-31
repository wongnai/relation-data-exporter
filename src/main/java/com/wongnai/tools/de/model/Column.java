package com.wongnai.tools.de.model;

import java.util.Set;

/**
 * Column.
 *
 * @author Suparit Krityakien
 */
public interface Column {
	/**
	 * Gets name.
	 *
	 * @return name
	 */
	String getName();

	/**
	 * Gets type from {@link java.sql.Types}.
	 *
	 * @return type
	 */
	int getType();

	/**
	 * Gets table.
	 *
	 * @return table
	 */
	Table getTable();

	/**
	 * Checks if this is primary key or not.
	 *
	 * @return {@code true} if this is primary key
	 */
	boolean isPrimaryKey();

	/**
	 * Gets reference this column refers to.
	 *
	 * @return reference this column refers to
	 */
	Reference getReferTo();

	/**
	 * Sets reference this column refers to.
	 *
	 * @param r
	 *            reference this column refers to
	 */
	void setReferTo(Reference r);

	/**
	 * Gets set of references this column is referred by.
	 *
	 * @return set of references this column is referred by
	 */
	Set<Reference> getReferredBys();

	/**
	 * Gets full name.
	 *
	 * @return full name
	 */
	String getFullName();
}
