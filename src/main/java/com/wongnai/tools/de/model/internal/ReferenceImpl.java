package com.wongnai.tools.de.model.internal;

import com.wongnai.tools.de.model.Column;
import com.wongnai.tools.de.model.Reference;

/**
 * The default implementation of {@link Reference}.
 *
 * @author Suparit Krityakien
 */
public class ReferenceImpl implements Reference {
	private final Column column;
	private final Column key;

	public ReferenceImpl(Column column, Column key) {
		this.column = column;
		this.key = key;
	}

	@Override
	public Column getColumn() {
		return column;
	}

	@Override
	public Column getKey() {
		return key;
	}

	@Override
	public String toString() {
		return "ReferenceImpl{" + "column=" + column + ", key=" + key + '}';
	}

	/**
	 * Creates reference.
	 *
	 * @param fk
	 *            fk
	 * @param pk
	 *            pk
	 * @return reference
	 */
	public static Reference create(Column fk, Column pk) {
		ReferenceImpl r = new ReferenceImpl(fk, pk);

		fk.setReferTo(r);
		pk.getReferredBys().add(r);

		return r;
	}
}
