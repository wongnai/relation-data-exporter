package com.wongnai.tools.de.settings;

import java.util.Objects;

/**
 * Reference.
 *
 * @author Suparit Krityakien
 */
public class Reference {
	private String pk;
	private String fk;
	private Referring referring;

	/**
	 * Gets primary key.
	 *
	 * @return primary key
	 */
	public String getPk() {
		return pk;
	}

	/**
	 * Sets primary key.
	 *
	 * @param pk
	 *            primary key
	 */
	public void setPk(String pk) {
		this.pk = pk;
	}

	/**
	 * Gets foreign key.
	 *
	 * @return foreign key
	 */
	public String getFk() {
		return fk;
	}

	/**
	 * Sets foreign key
	 *
	 * @param fk
	 *            foreign key
	 */
	public void setFk(String fk) {
		this.fk = fk;
	}

	/**
	 * Gets referring.
	 *
	 * @return referring
	 */
	public Referring getReferring() {
		return referring;
	}

	/**
	 * Sets referring.
	 *
	 * @param referring
	 *            referring
	 */
	public void setReferring(Referring referring) {
		this.referring = referring;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Reference reference = (Reference) o;
		return Objects.equals(fk, reference.fk) && Objects.equals(pk, reference.pk)
				&& Objects.equals(referring, reference.referring);
	}

	@Override
	public int hashCode() {
		return Objects.hash(fk, pk, referring);
	}

	@Override
	public String toString() {
		return "Reference{" + "fk='" + fk + '\'' + ", pk='" + pk + '\'' + ", referring=" + referring + '}';
	}

	/**
	 * Creates reference.
	 *
	 * @param fk
	 *            foreign key
	 * @param pk
	 *            primary key
	 * @param referring
	 *            referring
	 * @return reference
	 */
	public static Reference create(String fk, String pk, Referring referring) {
		Reference r = new Reference();
		r.setFk(fk);
		r.setPk(pk);
		r.setReferring(referring);
		return r;
	}
}
