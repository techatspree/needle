package de.akquinet.jbosscc.needle.db.configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PersistenceUnitConfiguration implements PersistenceConfiguration {

    private final EntityManagerFactory factory;
    private final EntityManager entityManager;

    public PersistenceUnitConfiguration(String puName) {
        factory = Persistence
            .createEntityManagerFactory(puName);
        entityManager = factory.createEntityManager();
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

}
