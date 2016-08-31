package com.wongnai.tools.de.service.internal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementCreator;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.wongnai.tools.de.ExceptionUtils;
import com.wongnai.tools.de.model.Column;
import com.wongnai.tools.de.model.Reference;
import com.wongnai.tools.de.model.Table;

import rx.Observable;
import rx.subjects.PublishSubject;

public class Data {
	private static final Logger LOGGER = LoggerFactory.getLogger(Data.class);
	private Context context;
	private Table table;
	private List<String> selectors;
	private Set<Key> keys;
	private PublishSubject<Object[]> newRows;
	private Set<Key> unloadedKeys;
	private Map<ReferenceSetting, Set<Key>> unloadedReferenceKeys;

	/**
	 * Constructs an instance.
	 *
	 * @param context
	 *            context
	 * @param table
	 *            table
	 */
	public Data(Context context, Table table) {
		this.context = context;
		this.table = table;
		this.selectors = new ArrayList<>();
		this.keys = new LinkedHashSet<>();
		this.newRows = PublishSubject.create();
		this.unloadedKeys = new LinkedHashSet<>();
		this.unloadedReferenceKeys = new LinkedHashMap<>();
	}

	/**
	 * Sets selectors.
	 *
	 * @param selectors
	 *            selectors
	 */
	public void setSelectors(List<String> selectors) {
		this.selectors = selectors;
	}

	/**
	 * Process data export.
	 */
	public void process() {
		this.selectors.forEach(s -> {
			LOGGER.debug("Fetching from " + table.getName() + " using selector " + s + "...");

			addQuery(s);
		});
		this.selectors.clear();
	}

	private void addQuery(String query) {
		StreamingStatementCreator ssc = createSql(query);
		try {
			context.getJdbcTemplate().query(ssc, this::addRow);
		} catch (Throwable t) {
			LOGGER.error("Cannot execute query : " + ssc, t);
			throw ExceptionUtils.wrap("Cannot execute query : " + ssc, t);
		}
	}

	private void addRow(ResultSet rs) throws SQLException {
		boolean added = false;
		Object[] row = new Object[table.getColumns().size()];
		Key key = extractRow(rs, row);

		if (keys.add(key)) {
			added = true;
			unloadedKeys.remove(key);
			for (Column pk : table.getColumns().getPrimaryKeys()) {
				pk.getReferredBys().forEach(r -> context.get(r.getColumn().getTable()).addReference(r, key));
			}
		}
		if (added) {
			newRows.onNext(row);
			if (keys.size() % 1000 == 0) {
				LOGGER.info(table.getName() + " is now " + keys.size() + ".");
			}
		}
	}

	private Key extractRow(ResultSet rs, Object[] row) throws SQLException {
		int n = table.getColumns().size();
		Key key = new Key();

		for (int i = 0; i < n; i++) {
			Column col = table.getColumns().get(i);
			Object o = transform(rs.getObject(i + 1), col);
			if (!rs.wasNull()) {
				row[i] = o;
				if (col.isPrimaryKey()) {
					key.add(o);
				}
				Reference r = col.getReferTo();
				if (r != null) {
					context.get(r.getKey().getTable()).addKey(Key.createSingle(o));
				}
			}
		}
		if (key.getVal() == null) {
			key = Key.createAll(row);
		}
		return key;
	}

	private Object transform(Object object, Column col) {
		return context.getTransformer(col).apply(object);
	}

	private void addReference(Reference r, Key key) {
		ReferenceSetting rs = context.getReferenceSetting(r);
		if (rs != null && rs.getReferring().isIncluded()) {
			Set<Key> rks = unloadedReferenceKeys.get(rs);

			if (rks == null) {
				rks = new LinkedHashSet<>();

				unloadedReferenceKeys.put(rs, rks);
			}

			rks.add(key);
		}
	}

	/**
	 * Adds key.
	 *
	 * @param key
	 *            key
	 * @return {@code true} if added
	 */
	public boolean addKey(Key key) {
		if (!keys.contains(key)) {
			return unloadedKeys.add(key);
		} else {
			return false;
		}
	}

	/**
	 * Gets new rows.
	 *
	 * @return new rows
	 */
	public Observable<Object[]> getNewRows() {
		return newRows;
	}

	private StreamingStatementCreator createSql(String query) {
		return new StreamingStatementCreator("SELECT " + table.getColumns().getSelectionText(table.getName()) + " FROM "
				+ table.getName() + " " + query + ";");
	}

	private void loadKeys(List<Key> newKeys) {
		if (!newKeys.isEmpty()) {
			if (table.getColumns().getPrimaryKeys().size() == 1) {
				addQuery("WHERE " + buildIdsClause(table.getColumns().getPrimaryKeys().get(0).getName(), newKeys));
			} else {
				LOGGER.warn("Ignored loaded unloaded composite keys.");
			}
		}
	}

	private static String buildIdsClause(String columnName, List<Key> keys) {
		return columnName + " in (" + Joiner.on(",").join(keys.stream().map(Key::getVal).collect(Collectors.toList()))
				+ ")";
	}

	/**
	 * Processes unloaded keys.
	 *
	 * @return {@code true} if processed
	 */
	public boolean processUnloadedKeys() {
		if (!unloadedKeys.isEmpty()) {
			ArrayList<Key> newKeys = new ArrayList<>(unloadedKeys);
			unloadedKeys.clear();

			LOGGER.debug("Fetching unloaded keys of " + table.getName() + "...");

			Lists.partition(newKeys, 1000).forEach(this::loadKeys);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Processes unloaded reference keys.
	 *
	 * @return {@code true} if processed
	 */
	public boolean processUnloadedReferenceKeys() {
		boolean dirty = false;

		for (Map.Entry<ReferenceSetting, Set<Key>> entry : new LinkedHashSet<>(unloadedReferenceKeys.entrySet())) {
			ReferenceSetting rs = entry.getKey();
			Set<Key> v = entry.getValue();

			ArrayList<Key> newKeys = new ArrayList<>(v);
			v.clear();

			if (!newKeys.isEmpty()) {
				dirty = true;

				LOGGER.debug("Fetching from referring " + rs.getRef().getColumn() + "...");

				Lists.partition(newKeys, rs.getReferring().getBatchSize()).forEach(l -> addQuery(
						"WHERE " + buildIdsClause(rs.getRef().getColumn().getName(), l) + getReferredByLimit(rs)));
			}
		}

		return dirty;
	}

	private String getReferredByLimit(ReferenceSetting rs) {
		if (rs.getReferring().getLimit() != null) {
			return " LIMIT " + rs.getReferring().getLimit();
		} else {
			return "";
		}
	}

	/**
	 * Logs export information.
	 */
	public void logExportInfo() {
		if (!keys.isEmpty()) {
			LOGGER.info(" " + table.getName() + " " + keys.size() + " rows.");
		}
	}

	private static class StreamingStatementCreator implements PreparedStatementCreator {
		private final String sql;

		StreamingStatementCreator(String sql) {
			this.sql = sql;
		}

		@Override
		public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
			try {
				PreparedStatement statement = connection.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY,
						ResultSet.CONCUR_READ_ONLY);
				statement.setFetchSize(Integer.MIN_VALUE);
				return statement;
			} catch (Exception e) {
				LOGGER.warn("JDBC Driver does not support cursor.", e);
				return connection.prepareStatement(sql);
			}
		}

		@Override
		public String toString() {
			return "StreamingStatementCreator{" + "sql='" + sql + '\'' + '}';
		}
	}
}
