package de.akquinet.jbosscc.needle;

import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;

/**
 * Thrown when an {@link ObjectUnderTest} instantiation fails, e.g. if the
 * associated class object has no corresponding constructor or the class is
 * abstract, a primitive type or an interface.
 *
 */
public class ObjectUnderTestInstantiationException extends Exception {

	private static final long serialVersionUID = 1L;

	public ObjectUnderTestInstantiationException() {
		super();
	}

	public ObjectUnderTestInstantiationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ObjectUnderTestInstantiationException(String message) {
		super(message);
	}

	public ObjectUnderTestInstantiationException(Throwable cause) {
		super(cause);
	}

}
