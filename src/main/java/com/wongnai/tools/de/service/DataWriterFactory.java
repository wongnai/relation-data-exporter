package com.wongnai.tools.de.service;

/**
 * Creates a writer for a given output.
 *
 * @author Suparit Krityakien
 */
public interface DataWriterFactory {
	/**
	 * Creates data writer.
	 *
	 * @param output
	 *            output
	 * @return data writer
	 */
	DataWriter create(String output);
}
