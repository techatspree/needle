package de.akquinet.jbosscc.needle.common;

import static java.lang.String.format;

/**
 * Utility class for checks and verifications. Inspired by guava/Preconditions, but without adding the external
 * dependency.
 * 
 * @author Jan Galinski, Holisticon AG
 */
public final class Preconditions {

    private Preconditions() {
        // avoid instantiation.
    }

    public static void checkState(final boolean expression, final String message, final Object... parameters) {
        if (!expression) {
            throw new IllegalStateException(format(message, parameters));
        }
    }
}
