package com.wongnai.tools.de.service;

import java.io.OutputStream;

import com.wongnai.tools.de.settings.Settings;

/**
 * Exporter.
 *
 * @author Suparit Krityakien
 */
public interface DataExporter {
	/**
	 * Exports.
	 *
	 * @param settings
	 *            settings
	 */
	void export(Settings settings);

	/**
	 * Prints usage.
	 *
	 * @param os
	 *            output stream
	 */
	void printConfig(OutputStream os);
}
