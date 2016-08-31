package com.wongnai.tools.de.model;

import java.util.List;
import java.util.Set;

/**
 * Metadata.
 *
 * @author Suparit Krityakien
 */
public interface Meta {
	/**
	 * Gets table.
	 *
	 * @param name
	 *            name
	 * @return table
	 */
	Table getTable(String name);

	/**
	 * Gets all tables.
	 */
	List<Table> getAllTables();

	/**
	 * Gets or adds reference by specifying pk and fk.
	 *
	 * @param fk
	 *            fk
	 * @param pk
	 *            pk
	 * @return reference
	 */
	Reference getOrAddReference(String fk, String pk);

	/**
	 * Gets references by primary key.
	 *
	 * @param pk
	 *            primary key
	 * @return references
	 */
	Set<Reference> getReferencesByKey(String pk);
}
