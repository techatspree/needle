package de.akquinet.jbosscc.needle.db;

import javax.persistence.EntityTransaction;

import de.akquinet.jbosscc.needle.injection.InjectionProvider;
import de.akquinet.jbosscc.needle.injection.InjectionTargetInformation;

class EntityTransactionProvider implements InjectionProvider<EntityTransaction> {

    private final DatabaseTestcase databaseTestcase;

    public EntityTransactionProvider(DatabaseTestcase databaseTestcase) {
        super();
        this.databaseTestcase = databaseTestcase;
    }

    @Override
    public boolean verify(InjectionTargetInformation information) {
        return information.getType() == EntityTransaction.class ? true : false;
    }

    @Override
    public EntityTransaction getInjectedObject(Class<?> injectionPointType) {
        return databaseTestcase.getEntityManager().getTransaction();
    }

    @Override
    public Object getKey(InjectionTargetInformation information) {
        return EntityTransaction.class;
    }
}
