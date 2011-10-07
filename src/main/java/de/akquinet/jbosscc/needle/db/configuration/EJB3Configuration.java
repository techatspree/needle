package de.akquinet.jbosscc.needle.db.configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.hibernate.ejb.Ejb3Configuration;

import de.akquinet.jbosscc.needle.configuration.NeedleConfiguration;

public class EJB3Configuration implements PersistenceConfiguration {

	private final EntityManagerFactory factory;
	private final EntityManager entityManager;

	public EJB3Configuration(final Class<?>[] entityClasses) {
		factory = createEntityManagerFactory(entityClasses);
		entityManager = EntityManagerProxyFactory.createProxy(factory.createEntityManager());
	}

	public EntityManager getEntityManager() {
		return entityManager;
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

	@Override
	public EntityManagerFactory getEntityManagerFactory() {
		return factory;
	}
}
