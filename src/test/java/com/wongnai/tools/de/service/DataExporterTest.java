package com.wongnai.tools.de.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.dbcp2.BasicDataSource;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import com.wongnai.tools.de.TestHelper;
import com.wongnai.tools.de.settings.Reference;
import com.wongnai.tools.de.settings.Referring;
import com.wongnai.tools.de.settings.Settings;
import com.wongnai.tools.de.settings.Table;
import com.wongnai.tools.de.settings.Transformation;
import com.wongnai.tools.de.spring.SpringConfig;

public class DataExporterTest {
	private DataExporter underTest;
	private BasicDataSource dataSource;
	private File tmpFile;
	private JdbcTemplate jdbcTemplate;

	@Before
	public void setUp() throws Exception {
		dataSource = TestHelper.createDataSource();
		jdbcTemplate = new JdbcTemplate(dataSource);

		SpringConfig c = new SpringConfig();
		c.setJdbcTemplate(jdbcTemplate);

		TestHelper.setUpDb(jdbcTemplate);
		TestHelper.setUpData(jdbcTemplate);

		tmpFile = File.createTempFile(DataExporterTest.class.getSimpleName(), "sql");

		underTest = c.dataExporter();
	}

	@After
	public void cleanUp() throws Exception {
		dataSource.close();
		tmpFile.delete();
	}

	@Test
	public void testBasic() throws Exception {
		Settings settings = createDefaultSettings();

		underTest.export(settings);

		String actual = TestHelper.read(new FileInputStream(tmpFile));

		Assert.assertThat(actual, CoreMatchers.containsString(readResource("expected1.sql")));
		Assert.assertThat(actual, CoreMatchers.containsString(readResource("expected2.sql")));
		Assert.assertThat(actual, CoreMatchers.containsString(readResource("expected3.sql")));
		Assert.assertThat(actual, CoreMatchers.containsString(readResource("expected4.sql")));
		Assert.assertThat(actual, CoreMatchers.containsString(readResource("expected5.sql")));
	}

	private Settings createDefaultSettings() {
		List<Table> table = new ArrayList<>();
		table.add(Table.create("TABLE_3", Arrays.asList("")));
		table.add(Table.create("TABLE_2", Arrays.asList("WHERE id = 5", " WHERE id = 6", " WHERE id = 5")));

		Settings settings = new Settings();
		settings.setOutput(tmpFile.getAbsolutePath());
		settings.setTables(table);

		List<Reference> references = new ArrayList<>();
		references.add(Reference.create("TABLE_4.T1_ID", "TABLE_1.ID", Referring.create(true, 100, 1000)));
		references.add(Reference.create("TABLE_5.T1_ID", "TABLE_1.ID", Referring.create(true, 100, 1000)));
		settings.setReferences(references);

		return settings;
	}

	private String readResource(String name) throws IOException {
		return TestHelper.read(this.getClass().getResourceAsStream(name));
	}

	@Test
	public void testBasicWithTransformations() throws Exception {
		Settings settings = createDefaultSettings();

		settings.setTransformations(
				Arrays.asList(Transformation.create("regex", ".*\\.NAME", "(.*)(name)(.*)", "$1 $2 $3")));

		underTest.export(settings);

		String actual = TestHelper.read(new FileInputStream(tmpFile));

		Assert.assertThat(actual, CoreMatchers.containsString(readResource("fexpected1.sql")));
		Assert.assertThat(actual, CoreMatchers.containsString(readResource("fexpected2.sql")));
		Assert.assertThat(actual, CoreMatchers.containsString(readResource("fexpected3.sql")));
		Assert.assertThat(actual, CoreMatchers.containsString(readResource("fexpected4.sql")));
		Assert.assertThat(actual, CoreMatchers.containsString(readResource("fexpected5.sql")));
	}
}
