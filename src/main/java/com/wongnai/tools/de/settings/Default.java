package com.wongnai.tools.de.settings;

import java.util.List;
import java.util.Objects;

/**
 * Default settings.
 *
 * @author Suparit Krityakien
 */
public class Default {
	private List<String> selectors;
	private Referring referring;

	/**
	 * Gets selectors.
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

	/**
	 * Gets referring.
	 *
	 * @return referring
	 */
	public Referring getReferring() {
		return referring;
	}

	/**
	 * Set referring.
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
		Default aDefault = (Default) o;
		return Objects.equals(selectors, aDefault.selectors) && Objects.equals(referring, aDefault.referring);
	}

	@Override
	public int hashCode() {
		return Objects.hash(selectors, referring);
	}

	@Override
	public String toString() {
		return "Default{" + "selectors=" + selectors + ", referring=" + referring + '}';
	}

	/**
	 * Creates default settings.
	 *
	 * @param selectors
	 *            selectors
	 * @param referring
	 *            referring
	 * @return default settings
	 */
	public static Default create(List<String> selectors, Referring referring) {
		Default d = new Default();
		d.setSelectors(selectors);
		d.setReferring(referring);

		return d;
	}
}
