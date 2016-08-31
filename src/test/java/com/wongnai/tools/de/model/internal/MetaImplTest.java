package com.wongnai.tools.de.model.internal;

import java.sql.Types;

import org.apache.commons.dbcp2.BasicDataSource;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import com.wongnai.tools.de.TestHelper;
import com.wongnai.tools.de.model.Column;
import com.wongnai.tools.de.model.Table;

public class MetaImplTest {
	private MetaImpl underTest;
	private BasicDataSource dataSource;

	@Before
	public void setUp() throws Exception {
		dataSource = TestHelper.createDataSource();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		TestHelper.setUpDb(jdbcTemplate);

		underTest = new MetaImpl(jdbcTemplate);
	}

	@After
	public void cleanUp() throws Exception {
		dataSource.close();
	}

	@Test
	public void testBasic() {
		Table table1 = underTest.getTable("TABLE_1");
		Assert.assertThat(table1.getName(), CoreMatchers.equalTo("TABLE_1"));
		assertColumn(table1.getColumns().get("ID"), "ID", Types.BIGINT, true);
		assertColumn(table1.getColumns().get("NAME"), "NAME", Types.VARCHAR, false);
		assertColumn(table1.getColumns().get("CREATION_TIME"), "CREATION_TIME", Types.TIMESTAMP, false);
		assertColumn(table1.getColumns().get("V1"), "V1", Types.DOUBLE, false);

		Table table2 = underTest.getTable("TABLE_2");
		Assert.assertThat(table2.getName(), CoreMatchers.equalTo("TABLE_2"));
		assertColumn(table2.getColumns().get("ID"), "ID", Types.BIGINT, true);
		assertColumn(table2.getColumns().get("NAME"), "NAME", Types.VARCHAR, false);
		assertColumn(table2.getColumns().get("CREATION_TIME"), "CREATION_TIME", Types.TIMESTAMP, false);
		assertColumn(table2.getColumns().get("T1_ID"), "T1_ID", Types.BIGINT, false);
		Assert.assertThat(table2.getColumns().get("T1_ID").getReferTo().getKey(),
				CoreMatchers.sameInstance(table1.getColumns().get("ID")));
		Assert.assertThat(table1.getColumns().get("ID").getReferredBys(),
				CoreMatchers.hasItem(table2.getColumns().get("T1_ID").getReferTo()));

		Table table3 = underTest.getTable("TABLE_3");
		Assert.assertThat(table3.getName(), CoreMatchers.equalTo("TABLE_3"));
		assertColumn(table3.getColumns().get("ID"), "ID", Types.BIGINT, true);
		assertColumn(table3.getColumns().get("NAME"), "NAME", Types.VARCHAR, false);
		assertColumn(table3.getColumns().get("CREATION_TIME"), "CREATION_TIME", Types.TIMESTAMP, false);
		assertColumn(table3.getColumns().get("T1_ID"), "T1_ID", Types.BIGINT, false);
		assertColumn(table3.getColumns().get("T2_ID"), "T2_ID", Types.BIGINT, false);
		Assert.assertThat(table3.getColumns().get("T1_ID").getReferTo().getKey(),
				CoreMatchers.sameInstance(table1.getColumns().get("ID")));
		Assert.assertThat(table1.getColumns().get("ID").getReferredBys(),
				CoreMatchers.hasItem(table3.getColumns().get("T1_ID").getReferTo()));
		Assert.assertThat(table3.getColumns().get("T2_ID").getReferTo().getKey(),
				CoreMatchers.sameInstance(table2.getColumns().get("ID")));
		Assert.assertThat(table2.getColumns().get("ID").getReferredBys(),
				CoreMatchers.hasItem(table3.getColumns().get("T2_ID").getReferTo()));
	}

	private void assertColumn(Column c, String name, int type, boolean primaryKey) {
		Assert.assertThat(c.getName(), CoreMatchers.equalTo(name));
		Assert.assertThat(c.getType(), CoreMatchers.equalTo(type));
		Assert.assertThat(c.isPrimaryKey(), CoreMatchers.equalTo(primaryKey));
	}
}
