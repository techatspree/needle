package de.akquinet.jbosscc.needle.db.configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public interface PersistenceConfiguration {

	EntityManager getEntityManager();

	EntityManagerFactory getEntityManagerFactory();

}
