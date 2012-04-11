package de.akquinet.jbosscc.needle.db.configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Creates an {@link EntityManagerFactory} and {@link EntityManager} for a named
 * persistence unit.
 */
class PersistenceUnitConfiguration implements PersistenceConfiguration {

	private final EntityManagerFactory factory;
	private final EntityManager entityManager;

	/**
	 * Creates an {@link EntityManagerFactory} and {@link EntityManager} for the
	 * named persistence unit.
	 *
	 * @param persistenceUnitName
	 *            the name of the persistence unit
	 */
	public PersistenceUnitConfiguration(String persistenceUnitName) {
		factory = Persistence.createEntityManagerFactory(persistenceUnitName);
		entityManager = EntityManagerProxyFactory.createProxy(factory.createEntityManager());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EntityManagerFactory getEntityManagerFactory() {
		return factory;
	}

}