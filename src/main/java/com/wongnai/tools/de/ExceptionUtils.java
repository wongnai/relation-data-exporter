package com.wongnai.tools.de;

import org.springframework.util.StringUtils;

/**
 * Utilities for working with exception.
 *
 * @author Suparit Krityakien
 */
public final class ExceptionUtils {
	private ExceptionUtils() {
	}

	/**
	 * Wraps exception with {@link RuntimeException} if the given throwable is
	 * checked exception.
	 *
	 * @param message
	 *            message
	 * @param throwable
	 *            throwable
	 *
	 * @return unchecked exception;
	 */
	public static RuntimeException wrap(String message, Throwable throwable) {
		if (throwable instanceof RuntimeException) {
			return (RuntimeException) throwable;
		} else if (!StringUtils.isEmpty(message)) {
			return new RuntimeException(message, throwable);
		} else {
			return new RuntimeException(throwable);
		}
	}
}
