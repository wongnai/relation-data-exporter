package com.wongnai.tools.de.model.internal;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;

import com.wongnai.tools.de.ExceptionUtils;
import com.wongnai.tools.de.model.Column;
import com.wongnai.tools.de.model.Meta;
import com.wongnai.tools.de.model.Reference;
import com.wongnai.tools.de.model.Table;

/**
 * The default implementation of {@link Meta}.
 *
 * @author Suparit Krityakien
 */
public class MetaImpl implements Meta {
	private static final Logger LOGGER = LoggerFactory.getLogger(MetaImpl.class);
	private JdbcTemplate jdbcTemplate;
	private Map<String, TableImpl> tables;
	private boolean allLoaded;

	/**
	 * Constructs an instance.
	 *
	 * @param jdbcTemplate
	 *            jdbc template
	 */
	public MetaImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		this.tables = new HashMap<>();
	}

	@Override
	public Table getTable(String name) {
		TableImpl table = tables.get(name);

		if (table == null) {
			table = new TableImpl(this, name);
			tables.put(name, table);
		}

		return table;
	}

	@Override
	public List<Table> getAllTables() {
		if (!allLoaded) {
			Set<String> tableNames = new LinkedHashSet<>();

			execute(metaData -> {
				try {
					try (ResultSet rs = metaData.getTables(null, null, "%", null)) {
						while (rs.next()) {
							tableNames.add(rs.getString("TABLE_NAME"));
						}
					}
				} catch (SQLException e) {
					throw ExceptionUtils.wrap("Cannot getVal tables.", e);
				}
			});

			tableNames.forEach(n -> {
				LOGGER.info("Loading table " + n + ".");

				Table t = getTable(n);

				t.getColumns();
			});

			allLoaded = true;
		}

		return new ArrayList<>(tables.values());
	}

	/**
	 * Gets tables.
	 *
	 * @return tables
	 */
	public Set<TableImpl> getTables() {
		return tables.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toSet());
	}

	@Override
	public Reference getOrAddReference(String fk, String pk) {
		Column fkc = getColumn(fk);

		if (fkc == null) {
			throw new RuntimeException(fk + " not found.");
		}
		Reference ref = fkc.getReferTo();

		if (ref == null) {
			return ReferenceImpl.create(fkc, getColumn(pk));
		} else {
			return ref;
		}
	}

	@Override
	public Set<Reference> getReferencesByKey(String pk) {
		Column pkc = getColumn(pk);

		return pkc.getReferredBys();
	}

	private Column getColumn(String fullColumnName) {
		String[] parts = fullColumnName.split("\\.");

		return getTable(parts[0]).getColumns().get(parts[1]);
	}

	/**
	 * Executes metadata consumer.
	 *
	 * @param consumer
	 *            consumer
	 */
	public void execute(Consumer<DatabaseMetaData> consumer) {
		jdbcTemplate.execute(new ConnectionCallback<Void>() {
			@Override
			public Void doInConnection(Connection con) throws SQLException, DataAccessException {
				consumer.accept(con.getMetaData());

				return null;
			}
		});
	}
}
