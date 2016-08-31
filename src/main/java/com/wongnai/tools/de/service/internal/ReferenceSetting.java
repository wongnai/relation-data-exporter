package com.wongnai.tools.de.service.internal;

import java.util.Objects;

import com.wongnai.tools.de.model.Reference;
import com.wongnai.tools.de.settings.Referring;

/**
 * Reference's setting.
 *
 * @author Suparit Krityakien
 */
public class ReferenceSetting {
	private Reference ref;
	private Referring referring;

	/**
	 * Gets reference.
	 *
	 * @return reference
	 */
	public Reference getRef() {
		return ref;
	}

	/**
	 * Gets referring.
	 *
	 * @return referring
	 */
	public Referring getReferring() {
		return referring;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ReferenceSetting that = (ReferenceSetting) o;
		return Objects.equals(ref, that.ref) && Objects.equals(referring, that.referring);
	}

	@Override
	public int hashCode() {
		return Objects.hash(ref, referring);
	}

	@Override
	public String toString() {
		return "ReferenceSetting{" + "ref=" + ref + ", referring=" + referring + '}';
	}

	/**
	 * Creates settings.
	 *
	 * @param ref
	 *            ref
	 * @param referring
	 *            referring
	 * @return settings
	 */
	public static ReferenceSetting create(Reference ref, Referring referring) {
		ReferenceSetting rs = new ReferenceSetting();
		rs.ref = ref;
		rs.referring = referring;

		return rs;
	}
}
