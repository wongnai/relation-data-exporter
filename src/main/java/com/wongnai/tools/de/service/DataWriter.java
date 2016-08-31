package com.wongnai.tools.de.service;

import java.io.Closeable;

import com.wongnai.tools.de.model.Table;

/**
 * Table writer.
 *
 * @author Suparit Krityakien
 */
public interface DataWriter extends Closeable {
	/**
	 * Write a row.
	 *
	 * @param table
	 *            table
	 * @param row
	 *            row
	 */
	void writeRow(Table table, Object[] row);
}
