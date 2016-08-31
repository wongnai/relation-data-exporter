package com.wongnai.tools.de.service;

import java.io.Flushable;

/**
 * SQL data.
 *
 * @author Suparit Krityakien
 */
public interface SqlData extends Flushable {
	/**
	 * Adds row.
	 *
	 * @param row
	 *            row
	 */
	void addRow(Object[] row);
}
