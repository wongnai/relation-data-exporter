package com.wongnai.tools.de.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import com.wongnai.tools.de.cmd.Application;
import com.wongnai.tools.de.service.DataExporter;
import com.wongnai.tools.de.service.DataWriterFactory;
import com.wongnai.tools.de.service.internal.DataExporterImpl;
import com.wongnai.tools.de.service.internal.DataWriterFactoryImpl;

@EnableAutoConfiguration
@org.springframework.context.annotation.Configuration
public class SpringConfig {
	private JdbcTemplate jdbcTemplate;

	/**
	 * Sets jdbc template.
	 *
	 * @param jdbcTemplate
	 *            jdbc template
	 */
	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Bean
	public ApplicationRunner applicationRunner() {
		Application a = new Application();
		a.setDataExporter(dataExporter());

		return a;
	}

	/**
	 * Table Exporter.
	 *
	 * @return data exporter
	 */
	@Bean
	public DataExporter dataExporter() {
		DataExporterImpl de = new DataExporterImpl();
		de.setJdbcTemplate(jdbcTemplate);
		de.setDataWriterFactory(dataWriterFactory());

		return de;
	}

	/**
	 * Table writer factory.
	 *
	 * @return data writer factory
	 */
	@Bean
	public DataWriterFactory dataWriterFactory() {
		return new DataWriterFactoryImpl();
	}
}
