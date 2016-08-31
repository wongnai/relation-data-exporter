package com.wongnai.tools.de;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StreamUtils;

import com.google.common.base.Charsets;

public final class TestHelper {
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

	private TestHelper() {
	}

	/**
	 * Reads from stream.
	 *
	 * @param is
	 *            input stream
	 * @return content
	 * @throws IOException
	 *             error
	 */
	public static String read(InputStream is) throws IOException {
		return StreamUtils.copyToString(is, Charsets.UTF_8);
	}

	/**
	 * Creates a new data source for testing.
	 *
	 * @return a new data source
	 */
	public static BasicDataSource createDataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName("org.hsqldb.jdbc.JDBCDriver");
		dataSource.setUrl("jdbc:hsqldb:mem:testdb" + System.currentTimeMillis());
		dataSource.setUsername("SA");
		dataSource.setPassword("");

		return dataSource;
	}

	/**
	 * Sets up test db.
	 *
	 * @param jdbcTemplate
	 *            jdbc template
	 */
	public static void setUpDb(JdbcTemplate jdbcTemplate) {
		jdbcTemplate.execute("CREATE TABLE TABLE_1 ( " + "ID BIGINT PRIMARY KEY," + " NAME VARCHAR(100),"
				+ " CREATION_TIME TIMESTAMP," + " V1 REAL" + " );");

		jdbcTemplate.execute("CREATE TABLE TABLE_2 ( " + "ID BIGINT PRIMARY KEY," + " NAME VARCHAR(100),"
				+ " CREATION_TIME TIMESTAMP," + " T1_ID BIGINT" + " );");
		jdbcTemplate.execute("ALTER TABLE TABLE_2 ADD FOREIGN KEY (T1_ID) REFERENCES TABLE_1 (ID);");

		jdbcTemplate.execute("CREATE TABLE TABLE_3 ( " + "ID BIGINT PRIMARY KEY," + " NAME VARCHAR(100),"
				+ " CREATION_TIME TIMESTAMP," + " T1_ID BIGINT," + " T2_ID BIGINT" + " );");
		jdbcTemplate.execute("ALTER TABLE TABLE_3 ADD FOREIGN KEY (T1_ID) REFERENCES TABLE_1 (ID);");
		jdbcTemplate.execute("ALTER TABLE TABLE_3 ADD FOREIGN KEY (T2_ID) REFERENCES TABLE_2 (ID);");

		jdbcTemplate.execute("CREATE TABLE TABLE_4 ( " + "ID BIGINT," + " NAME VARCHAR(100),"
				+ " CREATION_TIME TIMESTAMP," + " T1_ID BIGINT" + " );");
		jdbcTemplate.execute("ALTER TABLE TABLE_4 ADD FOREIGN KEY (T1_ID) REFERENCES TABLE_1 (ID);");

		jdbcTemplate.execute("CREATE TABLE TABLE_5 ( " + "ID BIGINT," + " NAME VARCHAR(100),"
				+ " CREATION_TIME TIMESTAMP," + " T1_ID BIGINT," + " PRIMARY KEY(ID, NAME)" + " );");
		jdbcTemplate.execute("ALTER TABLE TABLE_5 ADD FOREIGN KEY (T1_ID) REFERENCES TABLE_1 (ID);");
	}

	/**
	 * Sets up test data.
	 *
	 * @param jdbcTemplate
	 *            jdbc template
	 */
	public static void setUpData(JdbcTemplate jdbcTemplate) {
		for (int i = 1; i <= 20; i++) {
			insertIntoTable1(jdbcTemplate, i);
		}
		for (int i = 1; i <= 20; i++) {
			insertIntoTable2(jdbcTemplate, i, i > 5);
		}
		for (int i = 1; i <= 20; i++) {
			insertIntoTable3(jdbcTemplate, i, i > 10, i > 15);
		}
		for (int i = 1; i <= 20; i++) {
			insertIntoTable4(jdbcTemplate, i, i > 10);
		}
		for (int i = 1; i <= 20; i++) {
			insertIntoTable5(jdbcTemplate, i, i > 10);
		}
	}

	private static void insertIntoTable1(JdbcTemplate jdbcTemplate, long id) {
		jdbcTemplate.execute(String.format("INSERT INTO TABLE_1 VALUES(%s, '%s', '%s', %s)", id, "t1name\\" + id,
				LocalDateTime.of(LocalDate.of(2016, 8, 25), LocalTime.ofSecondOfDay(id + 100)).format(DATE_FORMATTER),
				id + 200));
	}

	private static void insertIntoTable2(JdbcTemplate jdbcTemplate, long id, boolean linkToTable1) {
		jdbcTemplate.execute(String.format("INSERT INTO TABLE_2 VALUES(%s, '%s', '%s', %s)", id, "t2name''" + id,
				LocalDateTime.of(LocalDate.of(2016, 8, 25), LocalTime.ofSecondOfDay(id + 200)).format(DATE_FORMATTER),
				linkToTable1 ? id - 5 : "null"));
	}

	private static void insertIntoTable3(JdbcTemplate jdbcTemplate, long id, boolean linkToTable1,
			boolean linkToTable2) {
		jdbcTemplate.execute(String.format("INSERT INTO TABLE_3 VALUES(%s, '%s', '%s', %s, %s)", id, "t3name" + id,
				LocalDateTime.of(LocalDate.of(2016, 8, 25), LocalTime.ofSecondOfDay(id + 300)).format(DATE_FORMATTER),
				linkToTable1 ? id - 10 : "null", linkToTable2 ? id - 15 : "null"));
	}

	private static void insertIntoTable4(JdbcTemplate jdbcTemplate, long id, boolean linkToTable1) {
		jdbcTemplate.execute(String.format("INSERT INTO TABLE_4 VALUES(%s, '%s', '%s', %s)", id, "t4name" + id,
				LocalDateTime.of(LocalDate.of(2016, 8, 25), LocalTime.ofSecondOfDay(id + 300)).format(DATE_FORMATTER),
				linkToTable1 ? id - 10 : "null"));
	}

	private static void insertIntoTable5(JdbcTemplate jdbcTemplate, long id, boolean linkToTable1) {
		jdbcTemplate.execute(String.format("INSERT INTO TABLE_5 VALUES(%s, '%s', '%s', %s)", id, "t5name" + id,
				LocalDateTime.of(LocalDate.of(2016, 8, 25), LocalTime.ofSecondOfDay(id + 300)).format(DATE_FORMATTER),
				linkToTable1 ? id - 10 : "null"));
	}
}
