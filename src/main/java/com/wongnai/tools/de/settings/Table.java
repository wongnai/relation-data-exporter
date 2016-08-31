package com.wongnai.tools.de.settings;

import java.util.List;
import java.util.Objects;

/**
 * Table settings.
 *
 * @author Suparit Krityakien
 */
public class Table {
	private String name;
	private List<String> selectors;

	/**
	 * Gets table name.
	 *
	 * @return table name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets table name.
	 *
	 * @param name
	 *            table name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets selectors.
	 *
	 * A selector is a string which can put after the SQL's "FROM" $table
	 * keyword. It mays include the where clause, group by, order by and limit.
	 *
	 * @return selectors
	 */
	public List<String> getSelectors() {
		return selectors;
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

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Table table = (Table) o;
		return Objects.equals(name, table.name) && Objects.equals(selectors, table.selectors);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, selectors);
	}

	@Override
	public String toString() {
		return "Table{" + "name='" + name + '\'' + ", selectors=" + selectors + '}';
	}

	/**
	 * Creates table settings.
	 *
	 * @param name
	 *            name
	 * @param export
	 *            export
	 * @return table settings
	 */
	public static Table create(String name, List<String> selectors) {
		Table t = new Table();
		t.setName(name);
		t.setSelectors(selectors);

		return t;
	}
}
