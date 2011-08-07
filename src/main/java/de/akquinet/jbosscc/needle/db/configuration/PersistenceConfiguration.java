package de.akquinet.jbosscc.needle.db.configuration;

import javax.persistence.EntityManager;

public interface PersistenceConfiguration {

	EntityManager getEntityManager();

}
