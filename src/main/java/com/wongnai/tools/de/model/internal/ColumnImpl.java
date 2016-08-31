package com.wongnai.tools.de.model.internal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;

import com.wongnai.tools.de.model.Column;
import com.wongnai.tools.de.model.Reference;
import com.wongnai.tools.de.model.Table;

/**
 * The default implementation of {@link Column}.
 *
 * @author Suparit Krityakien
 */
public class ColumnImpl implements Column {
	private final Table table;
	private final String name;
	private int type;
	private Reference referTo;
	private Set<Reference> referredBys;
	private boolean primary;

	/**
	 * Constructs an instance.
	 *
	 * @param table
	 *            table
	 * @param name
	 *            name
	 */
	ColumnImpl(Table table, String name) {
		this.table = table;
		this.name = name;
		this.referredBys = new LinkedHashSet<>();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public Table getTable() {
		return table;
	}

	@Override
	public boolean isPrimaryKey() {
		return primary;
	}

	/**
	 * Sets primary key.
	 *
	 * @param primary
	 *            primary key
	 */
	public void setPrimary(boolean primary) {
		this.primary = primary;
	}

	@Override
	public Reference getReferTo() {
		return referTo;
	}

	@Override
	public void setReferTo(Reference referTo) {
		this.referTo = referTo;
	}

	@Override
	public Set<Reference> getReferredBys() {
		return referredBys;
	}

	@Override
	public String getFullName() {
		return table.getName() + "." + name;
	}

	@Override
	public String toString() {
		return "ColumnImpl{" + "table=" + table + ", name='" + name + '\'' + '}';
	}

	/**
	 * Creates column.
	 *
	 * @param table
	 *            table
	 * @param rs
	 *            result set
	 * @return column
	 * @throws SQLException
	 *             error
	 */
	public static ColumnImpl create(Table table, ResultSet rs) throws SQLException {
		ColumnImpl c = new ColumnImpl(table, rs.getString("COLUMN_NAME"));
		c.setType(rs.getInt("DATA_TYPE"));

		return c;
	}
}
