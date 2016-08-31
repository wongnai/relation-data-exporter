package com.wongnai.tools.de.service.internal;

import java.io.OutputStream;
import java.util.ArrayList;

import org.springframework.jdbc.core.JdbcTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.wongnai.tools.de.ExceptionUtils;
import com.wongnai.tools.de.model.internal.MetaImpl;
import com.wongnai.tools.de.service.DataExporter;
import com.wongnai.tools.de.service.DataWriterFactory;
import com.wongnai.tools.de.settings.Referring;
import com.wongnai.tools.de.settings.Settings;

/**
 * The default implementation of {@link DataExporter}.
 *
 * @author Suparit Krityakien
 */
public class DataExporterImpl implements DataExporter {
	private JdbcTemplate jdbcTemplate;
	private DataWriterFactory dataWriterFactory;

	/**
	 * Sets jdbc template.
	 *
	 * @param jdbcTemplate
	 *            jdbc template
	 */
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * Sets data writer factory.
	 *
	 * @param dataWriterFactory
	 *            data writer factory
	 */
	public void setDataWriterFactory(DataWriterFactory dataWriterFactory) {
		this.dataWriterFactory = dataWriterFactory;
	}

	@Override
	public void export(Settings settings) {
		MetaImpl meta = new MetaImpl(jdbcTemplate);

		Context c = new Context(settings, jdbcTemplate, dataWriterFactory.create(settings.getOutput()));

		c.process(meta);
	}

	@Override
	public void printConfig(OutputStream os) {
		MetaImpl meta = new MetaImpl(jdbcTemplate);

		Settings settings = Settings.create();
		settings.setReferences(new ArrayList<>());

		meta.getAllTables().forEach(t -> t.getColumns().getPrimaryKeys().stream()
				.filter(k -> k != null && k.getReferredBys() != null).forEach(k -> {
					k.getReferredBys().forEach(r -> {
						com.wongnai.tools.de.settings.Reference r2 = com.wongnai.tools.de.settings.Reference.create(
								r.getColumn().getFullName(), r.getKey().getFullName(),
								Referring.create(false, 100, 1000));
						settings.getReferences().add(r2);
					});
				}));

		try {
			new ObjectMapper(new YAMLFactory()).writeValue(os, settings);
		} catch (Exception e) {
			throw ExceptionUtils.wrap("Cannot print yaml.", e);
		}
	}
}
