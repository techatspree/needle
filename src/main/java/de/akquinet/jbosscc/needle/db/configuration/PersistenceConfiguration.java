package de.akquinet.jbosscc.needle.db.configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * Abstraction for bootstrapping {@link EntityManagerFactory} and
 * {@link EntityManager}.
 */
interface PersistenceConfiguration {

	/**
	 * Returns an {@link EntityManager} instance.
	 *
	 * @return entityManager
	 */
	EntityManager getEntityManager();

	/**
	 * Returns the EntityManagerFactory.
	 *
	 * @return EntityManagerFactory
	 */
	EntityManagerFactory getEntityManagerFactory();

}
