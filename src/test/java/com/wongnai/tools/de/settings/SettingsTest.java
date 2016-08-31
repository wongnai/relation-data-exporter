package com.wongnai.tools.de.settings;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Charsets;

public class SettingsTest {
	private Settings underTest;

	@Before
	public void setUp() {
		underTest = new Settings();
	}

	@Test
	public void testOutput() {
		Assert.assertThat(underTest.getOutput(), CoreMatchers.nullValue());

		String out = "out";
		underTest.setOutput(out);

		Assert.assertThat(underTest.getOutput(), CoreMatchers.equalTo(out));
	}

	@Test
	public void testFromYaml() throws Exception {
		underTest = Settings
				.fromYaml(new ByteArrayInputStream("output: /test/test2/test3/test4.sql".getBytes(Charsets.UTF_8)));

		Assert.assertThat(underTest.getOutput(), CoreMatchers.equalTo("/test/test2/test3/test4.sql"));
		Assert.assertThat(underTest.getTables(), CoreMatchers.nullValue());
	}

	@Test
	public void testFromYamlAll() throws Exception {
		underTest = Settings.fromYaml(SettingsTest.class.getResourceAsStream("settings.yaml"));

		Assert.assertThat(underTest.getOutput(), CoreMatchers.equalTo("/test/test2/test3/test4.sql"));

		Assert.assertThat(underTest.getDefaultValues(),
				CoreMatchers.equalTo(Default.create(new ArrayList<>(), Referring.create(false, 10, null))));

		Assert.assertThat(underTest.getReferences(), CoreMatchers.equalTo(
				Arrays.asList(Reference.create("TABLE_2.t1_id", "TABLE_1.id", Referring.create(true, 100, 1000)))));

		Assert.assertThat(underTest.getTables().get(0),
				CoreMatchers.equalTo(Table.create("TABLE_2", Arrays.asList("id = 5", "id = 6", "id = 5"))));
		Assert.assertThat(underTest.getTables().get(1),
				CoreMatchers.equalTo(Table.create("TABLE_3", Arrays.asList("1 = 1"))));
	}
}
