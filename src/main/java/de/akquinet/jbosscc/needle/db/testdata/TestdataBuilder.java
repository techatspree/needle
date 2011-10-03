package de.akquinet.jbosscc.needle.db.testdata;

/**
 * Interface for an concrete TestDataBuilder implementation.
 *
 * @param <T>
 *            the type to build
 */
public interface TestdataBuilder<T> {

	/**
	 * Creates a new instance of type {@link T}
	 *
	 * @return a new instance of type {@link T}
	 */
	T build();

	/**
	 * Creates a new instance of type {@link T} and save the instance.
	 *
	 * @return a new persisted instance of type {@link T}
	 */
	T buildAndSave();

}
