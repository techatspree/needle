package de.akquinet.jbosscc.needle.common;

import static java.lang.String.format;

/**
 * Utility class for checks and verifications. Inspired by guava/Preconditions,
 * but without adding the external dependency.
 * 
 * @author Jan Galinski, Holisticon AG
 */
public final class Preconditions {

    private Preconditions() {
        // avoid instantiation.
    }

    /**
     * Throws an {@link IllegalStateException} with formatted message if
     * condition is not met.
     * 
     * @param condition
     *            a boolean condition that must be <code>true</code> to pass
     * @param message
     *            text to use as exception message
     * @param parameters
     *            optional parameters used in
     *            {@link String#format(String, Object...)}
     */
    public static void checkState(final boolean condition, final String message, final Object... parameters) {
        if (!condition) {
            throw new IllegalStateException(format(message, parameters));
        }
    }

    /**
     * Throws an {@link IllegalArgumentException} with formatted message if
     * condition is not met.
     * 
     * @param condition
     *            a boolean condition that must be <code>true</code> to pass
     * @param message
     *            text to use as exception message
     * @param parameters
     *            optional parameters used in
     *            {@link String#format(String, Object...)

     */
    public static void checkArgument(final boolean condition, final String message, final Object... parameters) {
        if (!condition) {
            throw new IllegalArgumentException(format(message, parameters));
        }
    }
}
