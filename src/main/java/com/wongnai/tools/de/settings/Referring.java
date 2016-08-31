package com.wongnai.tools.de.settings;

import java.util.Objects;

/**
 * Settings for referred by key.
 *
 * @author Suparit Krityakien
 */
public class Referring {
	private boolean included;
	private int batchSize = 1000;
	private Integer limit;

	/**
	 * Checks if the referring table's data should be included or not.
	 *
	 * @return {@code true} if the referring table's data should be included
	 */
	public boolean isIncluded() {
		return included;
	}

	/**
	 * Sets value indicating if the referring table's data should be included or
	 * not.
	 *
	 * @param included
	 *            value indicating if the referring table's data should be
	 *            included or not
	 */
	public void setIncluded(boolean included) {
		this.included = included;
	}

	/**
	 * Sets batch size of the keys used for fetching for referring table.
	 *
	 * @return batch size of the keys used for fetching for referring table
	 */
	public int getBatchSize() {
		return batchSize;
	}

	/**
	 * Sets batch size of the keys used for fetching for referring table.
	 *
	 * @param batchSize
	 *            batch size of the keys used for fetching for referring table
	 */
	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}

	/**
	 * Gets maximum number of rows limit per fetch.
	 *
	 * @return maximum number of rows limit per fetch
	 */
	public Integer getLimit() {
		return limit;
	}

	/**
	 * Sets maximum number of rows limit per fetch.
	 *
	 * @param limit
	 *            maximum number of rows limit per fetch
	 */
	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Referring that = (Referring) o;
		return included == that.included && batchSize == that.batchSize && Objects.equals(limit, that.limit);
	}

	@Override
	public int hashCode() {
		return Objects.hash(included, batchSize, limit);
	}

	@Override
	public String toString() {
		return "Referring{" + "included=" + included + ", batchSize=" + batchSize + ", limit=" + limit + '}';
	}

	/**
	 * Creates referred by.
	 *
	 * @param included
	 *            included
	 * @param batchSize
	 *            batch size
	 * @param limit
	 *            limit
	 * @return referred by
	 */
	public static Referring create(boolean included, int batchSize, Integer limit) {
		Referring rb = new Referring();
		rb.setIncluded(included);
		rb.setBatchSize(batchSize);
		rb.setLimit(limit);

		return rb;
	}
}
