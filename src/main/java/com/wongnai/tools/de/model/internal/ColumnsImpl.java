package com.wongnai.tools.de.model.internal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;

import com.google.common.base.Joiner;
import com.wongnai.tools.de.ExceptionUtils;
import com.wongnai.tools.de.model.Column;
import com.wongnai.tools.de.model.Columns;

/**
 * The default implementation of {@link Columns}.
 *
 * @author Suparit Krityakien
 */
public class ColumnsImpl extends AbstractList<Column> implements Columns {
	private MetaImpl meta;
	private TableImpl table;
	private List<ColumnImpl> columns;
	private Map<String, ColumnImpl> columnsByName;
	private List<Column> primaryKeys;

	public ColumnsImpl(MetaImpl meta, TableImpl table) {
		this.meta = meta;
		this.table = table;
		this.primaryKeys = new ArrayList<>();
	}

	@Override
	public Column get(int index) {
		return getColumns().get(index);
	}

	private List<ColumnImpl> getColumns() {
		if (columns == null) {
			init();
		}
		return columns;
	}

	@Override
	public Column get(String name) {
		return getColumnsByName().get(name);
	}

	private Map<String, ColumnImpl> getColumnsByName() {
		if (columnsByName == null) {
			init();
		}
		return columnsByName;
	}

	@Override
	public List<Column> getPrimaryKeys() {
		return primaryKeys;
	}

	@Override
	public int size() {
		return getColumns().size();
	}

	private void init() {
		columns = new ArrayList<>();
		columnsByName = new HashMap<>();

		meta.execute(md -> {
			try (ResultSet rs = md.getColumns(null, null, table.getName(), null)) {
				while (rs.next()) {
					ColumnImpl c = ColumnImpl.create(table, rs);
					columns.add(c);
					columnsByName.put(c.getName(), c);
				}
			} catch (SQLException e) {
				throw ExceptionUtils.wrap("Cannot getVal columns of table " + table.getName() + ".", e);
			}
			try (ResultSet rs = md.getPrimaryKeys(null, null, table.getName())) {
				while (rs.next()) {
					ColumnImpl pk = columnsByName.get(rs.getString("COLUMN_NAME"));
					pk.setPrimary(true);
					primaryKeys.add(pk);
				}
			} catch (SQLException e) {
				throw ExceptionUtils.wrap("Cannot getVal primary keys of table " + table.getName() + ".", e);
			}
		});
	}

	@Override
	public String getSelectionText(String alias) {
		String a = StringUtils.isEmpty(alias) ? "" : alias + ".";
		return Joiner.on(",").join(getColumns().stream().map(c -> a + c.getName()).collect(Collectors.toList()));
	}
}
