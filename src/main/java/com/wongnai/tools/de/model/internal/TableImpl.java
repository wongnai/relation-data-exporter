package com.wongnai.tools.de.model.internal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.wongnai.tools.de.ExceptionUtils;
import com.wongnai.tools.de.model.Columns;
import com.wongnai.tools.de.model.Reference;
import com.wongnai.tools.de.model.Table;

/**
 * The default implementation of {@link Table}.
 *
 * @author Suparit Krityakien
 */
public class TableImpl implements Table {
	private final String name;
	private final MetaImpl meta;
	private ColumnsImpl columns;
	private List<Reference> references;

	public TableImpl(MetaImpl meta, String name) {
		this.meta = meta;
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Columns getColumns() {
		if (columns == null) {
			init();
		}

		return columns;
	}

	private void init() {
		columns = new ColumnsImpl(meta, this);
		initReferences();
	}

	@Override
	public List<Reference> getReferences() {
		if (references == null) {
			init();
		}

		return references;
	}

	private void initReferences() {
		this.references = new ArrayList<>();

		meta.execute(md -> {
			try (ResultSet rs = md.getImportedKeys(null, null, getName())) {
				while (rs.next()) {
					references.add(ReferenceImpl.create(
							meta.getTable(rs.getString("FKTABLE_NAME")).getColumns().get(rs.getString("FKCOLUMN_NAME")),
							meta.getTable(rs.getString("PKTABLE_NAME")).getColumns()
									.get(rs.getString("PKCOLUMN_NAME"))));
				}
			} catch (SQLException e) {
				throw ExceptionUtils.wrap("Cannot getVal references of table " + name + ".", e);
			}
		});
	}

	@Override
	public String toString() {
		return "TableImpl{" + "name='" + name + '}';
	}
}
