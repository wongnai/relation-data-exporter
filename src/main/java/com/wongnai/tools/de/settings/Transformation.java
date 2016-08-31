package com.wongnai.tools.de.settings;

import java.util.Objects;

/**
 * Value transformation.
 *
 * @author Suparit Krityakien
 */
public class Transformation {
	private String type;
	private String column;
	private String from;
	private String to;

	/**
	 * Gets type.
	 *
	 * @return type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets type.
	 *
	 * @param type
	 *            type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Gets column pattern.
	 *
	 * e.g. TABLE_.\.NAME .
	 *
	 * @return column
	 */
	public String getColumn() {
		return column;
	}

	/**
	 * Sets column.
	 *
	 * @param column
	 *            column
	 */
	public void setColumn(String column) {
		this.column = column;
	}

	/**
	 * Gets from.
	 *
	 * @return from
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * Sets from.
	 *
	 * @param from
	 *            from
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * Gets to.
	 *
	 * @return to
	 */
	public String getTo() {
		return to;
	}

	/**
	 * Sets to.
	 *
	 * @param to
	 *            to
	 */
	public void setTo(String to) {
		this.to = to;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Transformation that = (Transformation) o;
		return Objects.equals(type, that.type) && Objects.equals(column, that.column) && Objects.equals(from, that.from)
				&& Objects.equals(to, that.to);
	}

	@Override
	public int hashCode() {
		return Objects.hash(type, column, from, to);
	}

	@Override
	public String toString() {
		return "Transformation{" + "type='" + type + '\'' + ", column='" + column + '\'' + ", from='" + from + '\''
				+ ", to='" + to + '\'' + '}';
	}

	/**
	 * Creates transformer.
	 *
	 * @param type
	 *            type
	 * @param column
	 *            column
	 * @param from
	 *            from
	 * @param to
	 *            to
	 * @return transformer
	 */
	public static Transformation create(String type, String column, String from, String to) {
		Transformation t = new Transformation();
		t.setType(type);
		t.setColumn(column);
		t.setFrom(from);
		t.setTo(to);

		return t;
	}
}
