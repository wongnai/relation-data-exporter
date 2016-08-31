package com.wongnai.tools.de.service.internal;

import java.util.Arrays;
import java.util.Objects;

/**
 * Key.
 *
 * @author Suparit Krityakien
 */
public class Key {
	private Object o;
	private Object[] os;
	private int count;

	/**
	 * Adds value.
	 *
	 * @param val
	 *            value
	 */
	public void add(Object val) {
		count++;
		if (count == 1) {
			this.o = val;
		} else {
			Object[] nos = new Object[count];

			nos[0] = this.o;
			if (os != null) {
				for (int i = 1; i < os.length - 1; i++) {
					nos[i] = os[i];
				}
			}
			nos[count - 1] = val;
			this.os = nos;
		}
	}

	/**
	 * Gets value.
	 *
	 * @return value
	 */
	public Object getVal() {
		return o;
	}

	@Override
	public boolean equals(Object o1) {
		if (this == o1) {
			return true;
		}
		if (o1 == null || getClass() != o1.getClass()) {
			return false;
		}
		Key keys = (Key) o1;
		return count == keys.count && Objects.equals(o, keys.o) && Arrays.equals(os, keys.os);
	}

	@Override
	public int hashCode() {
		return Objects.hash(o, os, count);
	}

	@Override
	public String toString() {
		return "Key{" + "o=" + o + ", os=" + Arrays.toString(os) + ", count=" + count + '}';
	}

	/**
	 * Creates key with single value .
	 *
	 * @param val
	 *            value
	 * @return keys
	 */
	public static Key createSingle(Object val) {
		Key keys = new Key();
		keys.add(val);

		return keys;
	}

	/**
	 * Creates key from whole row.
	 *
	 * @param row
	 *            row
	 * @return keys
	 */
	public static Key createAll(Object[] row) {
		return createSingle(Objects.hash(row));
	}
}
