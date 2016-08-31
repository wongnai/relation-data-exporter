package com.wongnai.tools.de.settings;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.wongnai.tools.de.ExceptionUtils;

/**
 * Settings.
 *
 * @author Suparit Krityakien
 */
public class Settings {
	private String output;
	@JsonProperty("default")
	private Default defaultValues;
	private List<Reference> references;
	private List<Table> tables;
	private List<Transformation> transformations;

	/**
	 * Gets output.
	 *
	 * @return output
	 */
	public String getOutput() {
		return output;
	}

	/**
	 * Sets output.
	 *
	 * @param output
	 *            output
	 */
	public void setOutput(String output) {
		this.output = output;
	}

	/**
	 * Gets default values.
	 *
	 * @return default values
	 */
	public Default getDefaultValues() {
		return defaultValues;
	}

	/**
	 * Sets default values.
	 *
	 * @param defaultValues
	 *            default values
	 */
	public void setDefaultValues(Default defaultValues) {
		this.defaultValues = defaultValues;
	}

	/**
	 * Gets references.
	 *
	 * @return references
	 */
	public List<Reference> getReferences() {
		return references;
	}

	/**
	 * Sets references.
	 *
	 * @param references
	 *            references
	 */
	public void setReferences(List<Reference> references) {
		this.references = references;
	}

	/**
	 * Gets transformations.
	 *
	 * @return transformations
	 */
	public List<Transformation> getTransformations() {
		return transformations;
	}

	/**
	 * Sets transformations.
	 *
	 * @param transformations
	 *            transformations
	 */
	public void setTransformations(List<Transformation> transformations) {
		this.transformations = transformations;
	}

	/**
	 * Gets tables.
	 *
	 * @return tables
	 */
	public List<Table> getTables() {
		return tables;
	}

	/**
	 * Sets tables.
	 *
	 * @param tables
	 *            tables
	 */
	public void setTables(List<Table> tables) {
		this.tables = tables;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Settings settings = (Settings) o;
		return Objects.equals(output, settings.output) && Objects.equals(defaultValues, settings.defaultValues)
				&& Objects.equals(references, settings.references) && Objects.equals(tables, settings.tables)
				&& Objects.equals(transformations, settings.transformations);
	}

	@Override
	public int hashCode() {
		return Objects.hash(output, defaultValues, references, tables, transformations);
	}

	@Override
	public String toString() {
		return "Settings{" + "output='" + output + '\'' + ", defaultValues=" + defaultValues + ", references="
				+ references + ", tables=" + tables + ", transformations=" + transformations + '}';
	}

	/**
	 * Creates default settings.
	 *
	 * @return default settings
	 */
	public static Settings create() {
		Settings s = new Settings();
		s.setDefaultValues(Default.create(null, Referring.create(false, 100, 1000)));

		return s;
	}

	/**
	 * Creates settings from given yaml input stream.
	 *
	 * @param is
	 *            input stream
	 * @return settings
	 */
	public static Settings fromYaml(InputStream is) {
		try {
			return new ObjectMapper(new YAMLFactory()).readValue(is, Settings.class);
		} catch (Exception e) {
			throw ExceptionUtils.wrap("Cannot parse yaml.", e);
		}
	}
}
