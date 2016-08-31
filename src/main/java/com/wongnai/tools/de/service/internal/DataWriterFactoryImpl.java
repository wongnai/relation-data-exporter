package com.wongnai.tools.de.service.internal;

import com.wongnai.tools.de.service.DataWriter;
import com.wongnai.tools.de.service.DataWriterFactory;

/**
 * The default implementation of {@link DataWriterFactory}
 *
 * @author Suparit Krityakien
 */
public class DataWriterFactoryImpl implements DataWriterFactory {
	@Override
	public DataWriter create(String output) {
		return new DataWriterImpl(output);
	}
}
