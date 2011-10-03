package de.akquinet.jbosscc.needle.db.testdata;

import javax.persistence.EntityManager;

import de.akquinet.jbosscc.needle.db.transaction.TransactionHelper;

/**
 * A abstract implementation of {@link TestdataBuilder}.
 *
 * <pre>
 *
 * Implementation example:
 *
 * public class PersonTestDataBuilder extends AbstractTestdataBuilder<Person> {
 *
 *  ...
 *
 * 	public PersonTestDataBuilder() {
 * 	 super();
 * 	}
 *
 * 	public PersonTestDataBuilder(EntityManager entityManager) {
 * 	 super(entityManager);
 * 	}
 *
 * 	public PersonTestDataBuilder withName(String name){
 * 	 this.withName = name;
 * 	 return this;
 * 	}
 *
 * 	public PersonTestDataBuilder withAddress(Address address){
 * 	 this.withName = name;
 * 	 return this;
 * 	}
 *
 * 	public Person build() {
 * 	 Person person = new Person();
 * 	 ...
 * 	 return person;
 * 	}
 *
 *
 * Usage example:
 *
 * Person transientPerson = new PersonTestDataBuilder(em).build();
 * Person persistedPerson = new PersonTestDataBuilder(em).buildAndSave();
 * new PersonTestDataBuilder(em).withAddress(address).buildAndSave();
 *
 *
 *
 * </pre>
 *
 * @param <T>
 *            The type of the object to build.
 */
public abstract class AbstractTestdataBuilder<T> implements TestdataBuilder<T> {
	private static int count = 0;

	private EntityManager entityManager;

	private TransactionHelper transactionHelper;

	/**
	 * Creates an new {@link TestdataBuilder} with persistence.
	 *
	 * @param entityManager
	 */
	public AbstractTestdataBuilder(final EntityManager entityManager) {
		this.entityManager = entityManager;
		this.transactionHelper = new TransactionHelper(entityManager);
	}

	/**
	 * Creates an new {@link TestdataBuilder} without persistence.
	 */
	public AbstractTestdataBuilder() {
	}

	/**
	 * Returns the EntityManager or null.
	 *
	 * @return {@link EntityManager} or null
	 */
	protected final EntityManager getEntityManager() {
		return entityManager;
	}

	/**
	 * Returns whether the {@link TestdataBuilder} is constructed with a
	 * {@link EntityManager}
	 *
	 * @return true if {@link EntityManager} is available, else false
	 */
	protected final boolean hasEntityManager() {
		return entityManager != null;
	}

	/**
	 * Ensure the {@link TestdataBuilder} is constructed with a
	 * {@link EntityManager}
	 *
	 * @throws IllegalStateException
	 *             if the {@link TestdataBuilder} is constructed without a
	 *             {@link EntityManager}
	 */
	protected final void ensureEntityManager() {
		if (entityManager == null) {
			throw new IllegalStateException("cannot persist w/o entity manager!");
		}
	}

	/**
	 * {@inheritDoc} Executed within a new transaction.
	 *
	 * @throws IllegalStateException
	 *             if the {@link TestdataBuilder} is constructed without a
	 *             {@link EntityManager}
	 */
	@Override
	public final T buildAndSave() {
		ensureEntityManager();
		try {
			return transactionHelper.saveObject(build());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns an integer value from an static counter.
	 *
	 * @return value of the static counter.
	 */
	protected final int getId() {
		return count++;
	}
}
