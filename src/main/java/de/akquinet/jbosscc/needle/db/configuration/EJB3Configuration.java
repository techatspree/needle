package de.akquinet.jbosscc.needle.db.configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.hibernate.ejb.Ejb3Configuration;

import de.akquinet.jbosscc.needle.configuration.NeedleConfiguration;

/**
 * Allow a fine configuration of an EntityManagerFactory.
 */
class EJB3Configuration implements PersistenceConfiguration {

	private final EntityManagerFactory factory;
	private final EntityManager entityManager;

	/**
	 * Create an {@link EntityManagerFactory} and {@link EntityManager} for the
	 * given entity classes by using the configured hibernate specific
	 * configuration file (*cfg.xml).
	 *
	 * @param entityClasses
	 *            the entity classes
	 */
	public EJB3Configuration(final Class<?>[] entityClasses) {
		factory = createEntityManagerFactory(entityClasses);
		entityManager = EntityManagerProxyFactory.createProxy(factory.createEntityManager());
	}

	/**
	 * {@inheritDoc}
	 */
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

	private static EntityManagerFactory createEntityManagerFactory(Class<?>[] entityClasses) {
		final Ejb3Configuration cfg = new Ejb3Configuration();

		// add a regular hibernate.cfg.xml
		cfg.configure(NeedleConfiguration.getHibernateCfgFilename());

		for (Class<?> clazz : entityClasses) {
			cfg.addAnnotatedClass(clazz);
		}

		return cfg.buildEntityManagerFactory();

	}
}
